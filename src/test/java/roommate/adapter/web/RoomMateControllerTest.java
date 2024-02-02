package roommate.adapter.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RoomMateControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Startseite ist unter /index erreichbar")
    void index() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Die Arbeitsplatz buchen Seite ist unter /workspace_booking erreichbar")
    void workspaceBooking() throws Exception {
        MvcResult result = mvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Die Raumübersicht Seite ist unter /room_overview erreichbar")
    void roomOverview() throws Exception {
        MvcResult result = mvc.perform(get("/room_overview"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Die Buchungsübersicht Seite ist unter /user_bookings erreichbar")
    void userBookings() throws Exception {
        MvcResult result = mvc.perform(get("/user_bookings"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Die Buchungsregeln Seite ist unter /rules_booking erreichbar")
    void rulesBooking() throws Exception {
        MvcResult result = mvc.perform(get("/rules_booking"))
                .andExpect(status().isOk())
                .andReturn();
    }
}