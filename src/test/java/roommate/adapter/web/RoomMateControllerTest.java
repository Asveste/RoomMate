package roommate.adapter.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import roommate.adapter.security.MethodSecurityConfig;
import roommate.adapter.security.SecurityConfig;
import roommate.adapter.security.WithMockOAuth2User;
import roommate.applicationservice.BookingService;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomMateController.class)
@Import({SecurityConfig.class, MethodSecurityConfig.class})
public class RoomMateControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingService service;

    @Test
    @DisplayName("Die Startseite ist unter /index erreichbar")
    @WithMockOAuth2User(login = "JoeSchmoe")
    void index() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Arbeitsplatz Übersicht Seite ist unter /workspace_overview erreichbar")
    @WithMockOAuth2User(login = "JoeSchmoe")
    void workspaceOverview() throws Exception {
        mvc.perform(get("/workspace_overview"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Raumübersicht Seite ist unter /room_overview erreichbar")
    @WithMockOAuth2User(login = "JoeSchmoe")
    void roomOverview() throws Exception {
        mvc.perform(get("/room_overview"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Buchungsübersicht Seite ist unter /user_bookings erreichbar")
    @WithMockOAuth2User(login = "JoeSchmoe")
    void userBookings() throws Exception {
        mvc.perform(get("/user_bookings"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Die Buchungsregeln Seite ist unter /rules_booking erreichbar")
    @WithMockOAuth2User(login = "JoeSchmoe")
    void rulesBooking() throws Exception {
        mvc.perform(get("/rules_booking"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockOAuth2User(login = "JoeSchmoe")
    @DisplayName("Die Workspace Editor Seite ist nicht für reguläre Nutzer erreichbar")
    void workspaceEditor() throws Exception {
        mvc.perform(get("/workspace_editor").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockOAuth2User(login = "JoeSchmoe", roles = {"ADMIN"})
    @DisplayName("Die Workspace Editor Seite ist für ADMIN Nutzer erreichbar")
    void workspaceEditor2() throws Exception {
        mvc.perform(get("/workspace_editor").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testen ob Authentifizierung möglich ist")
    void test() throws Exception {
        mvc.perform(get("/"))
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/github"));
    }

    @Test
    @WithMockOAuth2User(login = "JoeSchmoe")
    @DisplayName("/workspace_details/{id} ist erreichbar")
    void workspaceBooking2() throws Exception {
        int workspaceId = 1;
        Workspace workspace = new Workspace(workspaceId, UUID.randomUUID(),
                List.of(new Trait("Katze")),
                List.of(new Timespan(LocalDate.now(),
                        LocalTime.of(10, 0),
                        LocalTime.of(12, 0), 1)));

        when(service.workspace(workspaceId)).thenReturn(workspace);

        mvc.perform(get("/workspace_details/{id}", workspaceId))
                .andExpect(status().isOk())
                .andExpect(view().name("workspace_details"))
                .andExpect(model().attribute("workspaceId", workspaceId))
                .andExpect(model().attribute("traits", workspace.traits()))
                .andExpect(model().attribute("existingReservations", workspace.existingReservations()))
                .andExpect(model().attribute("workspace", workspace));
        verify(service).workspace(workspaceId);
    }
}