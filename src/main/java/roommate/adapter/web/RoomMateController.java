package roommate.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomMateController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/room_overview")
    public String roomOverview() {
        return "room_overview";
    }
}
