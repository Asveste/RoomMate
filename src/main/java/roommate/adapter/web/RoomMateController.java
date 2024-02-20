package roommate.adapter.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roommate.applicationservice.BookingService;
import roommate.applicationservice.InvalidInput;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;
import roommate.domain.validation.onPost;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    public String workspaceBooking(Model model, Timespan timespan, BindingResult bindingResult,
                                   @RequestParam(required = false) String action, HttpServletRequest request) {
        List<Workspace> everyWorkspace = service.allWorkspaces();
        List<Workspace> filteredWorkspaces;
        List<Trait> allTraits = service.allTraitsFromWorkspaces(everyWorkspace);

        allTraits = allTraits.stream().distinct().toList();

        if ("filter".equals(action)) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("workspaces", everyWorkspace);
                model.addAttribute("allTraits", allTraits);
                return "workspace_booking";
            }

            String[] traitFilters = request.getParameterValues("traitFilter");
            List<String> traitFilterList = traitFilters != null ? Arrays.asList(traitFilters) : Collections.emptyList();

            if (!traitFilterList.isEmpty()) {
                everyWorkspace = everyWorkspace.stream()
                        .filter(workspace -> workspace.traits().stream()
                                .map(Trait::trait)
                                .anyMatch(traitFilterList::contains))
                        .collect(Collectors.toList());
            }

            if (timespan.date() != null && timespan.startTime() != null && timespan.endTime() != null) {
                filteredWorkspaces = everyWorkspace.stream()
                        .filter(workspace -> !workspace.isOverlap(timespan))
                        .toList();
            } else {
                filteredWorkspaces = everyWorkspace;
            }
        } else {
            filteredWorkspaces = everyWorkspace;
        }

        model.addAttribute("workspaces", filteredWorkspaces);
        model.addAttribute("allTraits", allTraits);

        return "workspace_booking";
    }

    @GetMapping("/room_details/{id}")
    public String roomDetails(Model model, @PathVariable("id") Integer roomId) {

        Workspace workspace = service.workspace(roomId);

        model.addAttribute("workspaceId", roomId);
        model.addAttribute("workspaces", workspace);

        return "room_details";
    }

    @PostMapping("/room_details/{id}")
    public String roomDetailsMakeReservation(Model model, @PathVariable("id") Integer roomId, @Validated(onPost.class) Timespan timespan,
                                             BindingResult bindingResult, boolean recurringReservation) {
        model.addAttribute("workspaceId", roomId);

        if (bindingResult.hasErrors()) {
            return "redirect:/room_details/" + roomId;
        }

        if (timespan.date() != null && timespan.date().isBefore(LocalDate.now())) {
            return "redirect:/room_details/" + roomId;
        }

//        if (timespan.startTime().isAfter(timespan.endTime()) || timespan.date().isBefore(LocalDate.now())) {
//            System.out.println("Invalid");
//            return "redirect:/room_details/" + roomId;
//        }

        if (recurringReservation) {
            try {
                service.addRecurringReservation(roomId, timespan);
                return "redirect:/success";
            } catch (InvalidInput e) {
                return "redirect:/room_details/" + roomId;
            }
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

        List<Workspace> workspaces = service.allWorkspaces();

        model.addAttribute("workspaces", workspaces);

        return "workspace_editor";
    }

    @GetMapping("/workspace_editor/{id}")
    @Secured("ROLE_ADMIN")
    public String workspaceEditorEdit(Model model, @PathVariable("id") Integer roomId) {
        Workspace workspace = service.workspace(roomId);

        model.addAttribute("workspaceId", roomId);
        model.addAttribute("workspaces", workspace);

        return "workspace_editor";
    }
}
