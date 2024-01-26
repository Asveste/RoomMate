package roommate.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomMateController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/workspace_booking")
    public String workspaceBooking() {
        return "workspace_booking";
    }

    @GetMapping("/room_overview")
    public String roomOverview() {
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
}
