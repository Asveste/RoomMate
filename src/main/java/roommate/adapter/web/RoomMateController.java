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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import roommate.applicationservice.BookingService;
import roommate.applicationservice.InvalidInput;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;
import roommate.domain.validation.onPost;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/workspace_overview")
    public String workspaceBooking(Model model, Timespan timespan, BindingResult bindingResult,
                                   @RequestParam(required = false) String action, HttpServletRequest request) {
        if ("filter".equals(action) && !bindingResult.hasErrors()) {
            List<Workspace> everyWorkspace = service.allWorkspaces();
            List<Trait> allTraits = service.allTraitsFromWorkspaces(everyWorkspace);

            // Filtern nach Traits
            List<Trait> traitFilters = Optional.ofNullable(request.getParameterValues("traitFilter"))
                    .stream().flatMap(Arrays::stream).map(Trait::new).collect(Collectors.toList());

            List<Workspace> filteredWorkspaces = service.filterWorkspacesByTraits(everyWorkspace, traitFilters);

            // Filtern nach Timespan
            if (timespan.date() != null || (timespan.startTime() != null && timespan.endTime() != null)) {
                filteredWorkspaces = service.filterWorkspacesByTimespan(filteredWorkspaces, timespan);
            }

            model.addAttribute("workspaces", filteredWorkspaces);
            model.addAttribute("allTraits", allTraits);
        } else {
            model.addAttribute("workspaces", service.allWorkspaces());
            model.addAttribute("allTraits", service.allTraitsFromWorkspaces(service.allWorkspaces()));
        }

        return "workspace_overview";
    }

    @GetMapping("/workspace_details/{id}")
    public String workspaceDetails(Model model, @PathVariable("id") Integer roomId) {
        Workspace workspace = service.workspace(roomId);

        model.addAttribute("workspaceId", roomId);
        model.addAttribute("workspaces", workspace);

        return "workspace_details";
    }

    @PostMapping("/workspace_details/{id}")
    public String workspaceDetailsMakeReservation(Model model, @PathVariable("id") Integer roomId,
                                                  @Validated(onPost.class) Timespan timespan,
                                                  BindingResult bindingResult, boolean recurringReservation) {
        model.addAttribute("workspaceId", roomId);
        Workspace workspace = service.workspace(roomId);

        if (bindingResult.hasErrors() ||
                (timespan.date() != null && timespan.date().isBefore(LocalDate.now()))
                || workspace.isOverlap(timespan)
                || !workspace.isValid(timespan)) {
            return "redirect:/workspace_details/" + roomId;
        }

        if (recurringReservation) {
            try {
                service.addRecurringReservation(roomId, timespan);
                return "redirect:/success";
            } catch (InvalidInput e) {
                return "redirect:/workspace_details/" + roomId;
            }
        }

        try {
            service.addReservation(roomId, timespan);
            return "redirect:/success";
        } catch (InvalidInput e) {
            return "redirect:/workspace_details/" + roomId;
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

    @GetMapping("/workspace_editor/modify/{id}")
    @Secured("ROLE_ADMIN")
    public String workspaceEditorEdit(Model model, @PathVariable("id") Integer roomId) {
        Workspace workspace = service.workspace(roomId);

        model.addAttribute("workspace", workspace);
        model.addAttribute("traits", workspace.traits());
        model.addAttribute("reservations", workspace.existingReservations());

        return "workspace_modify";
    }

    @PostMapping("/workspace_editor/modify/{id}/addTrait")
    @Secured("ROLE_ADMIN")
    public String addTraitToWorkspace(@PathVariable("id") Integer id, @RequestParam("trait") String trait, RedirectAttributes redirectAttributes) {
        try {
            service.addTraitAdmin(id, trait);
            redirectAttributes.addFlashAttribute("successMessage", "Trait erfolgreich hinzugefügt.");
        } catch (InvalidInput e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Hinzufügen des Traits.");
        }
        return "redirect:/workspace_editor/modify/{id}";
    }

    @PostMapping("/workspace_editor/modify/{id}/removeTrait")
    @Secured("ROLE_ADMIN")
    public String removeTraitFromWorkspace(@PathVariable("id") Integer id, @RequestParam("trait") String trait, RedirectAttributes redirectAttributes) {
        try {
            service.deleteTraitAdmin(id, trait);
            redirectAttributes.addFlashAttribute("successMessage", "Trait erfolgreich entfernt.");
        } catch (InvalidInput e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Entfernen des Traits.");
        }
        return "redirect:/workspace_editor/modify/{id}";
    }

    @PostMapping("/workspace_editor/modify/{id}/lockWorkspace")
    @Secured("ROLE_ADMIN")
    public String lockWorkspace(@PathVariable("id") Integer id, @Validated(onPost.class) Timespan timespan, RedirectAttributes redirectAttributes) {
        try {
            service.lockWorkspaceAdmin(id, timespan);
            redirectAttributes.addFlashAttribute("successMessage", "Arbeitsplatz erfolgreich gesperrt.");
        } catch (InvalidInput e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Sperren des Arbeitsplatzes.");
        }
        return "redirect:/workspace_editor/modify/{id}";
    }

    @PostMapping("/workspace_editor/modify/{id}/overwriteReservation")
    @Secured("ROLE_ADMIN")
    public String overwriteReservations(@PathVariable("id") Integer id, @Validated(onPost.class) Timespan timespan, RedirectAttributes redirectAttributes) {
        try {
            service.overwriteReservationsAdmin(id, timespan);
            redirectAttributes.addFlashAttribute("successMessage", "Reservierung erfolgreich überschrieben.");
        } catch (InvalidInput e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Überschreiben der Reservierung.");
        }
        return "redirect:/workspace_editor/modify/{id}";
    }

    @PostMapping("/workspace_editor/modify/{id}/cancelReservation")
    @Secured("ROLE_ADMIN")
    public String cancelReservation(@PathVariable("id") Integer id, Timespan timespan, RedirectAttributes redirectAttributes) {
        try {
            service.cancelReservationAdmin(id, timespan);
            redirectAttributes.addFlashAttribute("successMessage", "Reservierung erfolgreich storniert.");
        } catch (InvalidInput e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fehler beim Stornieren der Reservierung.");
        }
        return "redirect:/workspace_editor/modify/{id}";
    }
}
