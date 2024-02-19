package roommate.adapter.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import roommate.applicationservice.BookingService;
import roommate.applicationservice.InvalidInput;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RoomMateController {
    private final BookingService service;

    public RoomMateController(BookingService service) {
        this.service = service;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/workspace_booking")
    public String workspaceBooking(Model model, @Valid Timespan timespan,
                                   boolean recurringReservation, HttpServletRequest request,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "workspace_booking";
        }

        String[] traitFilters = request.getParameterValues("traitFilter");
        List<String> traitFilterList = traitFilters != null ? Arrays.asList(traitFilters) : Collections.emptyList();

        List<Workspace> workspaces = service.allWorkspaces();
        List<Workspace> filteredWorkspaces = new ArrayList<>();

        if (!traitFilterList.isEmpty()) {
            workspaces = workspaces.stream()
                    .filter(workspace -> workspace.traits().stream()
                            .map(Trait::trait)
                            .anyMatch(traitFilterList::contains))
                    .collect(Collectors.toList());
            System.out.println("Es wird gefiltert nach Traits");
        }

        if (timespan.date() != null && timespan.startTime() != null && timespan.endTime() != null) {
            filteredWorkspaces = workspaces.stream()
                    .filter(workspace -> !workspace.isOverlap(timespan))
                    .toList();
        }

        List<Trait> allTraits = workspaces.stream()
                .flatMap(workspace -> workspace.traits().stream())
                .toList();

        model.addAttribute("workspaces", workspaces);
        model.addAttribute("allTraits", allTraits);

        return "workspace_booking";
    }

    @GetMapping("/room_details/{id}")
    public String roomDetails(Model model, @PathVariable("id") Integer roomId) {
        List<Workspace> workspaces = service.allWorkspaces();

        Optional<Workspace> matchingWorkspace = workspaces
                .stream().filter(workspace -> Objects.equals(workspace.id(), roomId)).findFirst();

        Workspace workspace = matchingWorkspace.get();

        model.addAttribute("workspaceId", roomId);
        model.addAttribute("workspaces", workspace);
        return "room_details";
    }

    @PostMapping("/room_details/{id}")
    public String roomDetailsBooking(Model model, @PathVariable("id") Integer roomId, @Valid Timespan timespan, BindingResult bindingResult) {
        model.addAttribute("workspaceId", roomId);
        if (timespan.startTime().isAfter(timespan.endTime()) || timespan.date().isBefore(LocalDate.now())) {
            System.out.println("Invalid");
            return "redirect:/room_details/" + roomId;
        }
        if (bindingResult.hasErrors()) {
            return "room_details";
        }
        try {
            service.addReservation(roomId, timespan);
            return "redirect:/success";
        } catch (InvalidInput e) {
            return "redirect:/room_details/" + roomId;
        }
    }

    @GetMapping("/success")
    public String bookingSuccessful() {
        return "success";
    }

    @GetMapping("/room_overview")
    public String roomOverview(Model model) {
        List<Workspace> workspaces = service.allWorkspaces();
        model.addAttribute("workspaces", workspaces);
        return "room_overview";
    }

    @GetMapping("/user_bookings")
    public String userBookings() {
        return "user_bookings";
    }

    @GetMapping("/rules_booking")
    public String rulesBooking() {
        return "rules_booking";
    }

    @GetMapping("/workspace_editor")
    @Secured("ROLE_ADMIN")
    public String workspaceEditor(OAuth2AuthenticationToken auth, Model model) {
        String login = auth.getPrincipal().getAttribute("login");
        model.addAttribute("name", login);
        return "workspace_editor";
    }
}
