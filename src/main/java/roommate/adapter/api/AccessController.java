package roommate.adapter.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AccessController {

    @GetMapping("/api/access")
    public @ResponseBody List<Access> index() {


        // Keine Ahnung wie sowas geht :((( Also halt fr nicht...
        RestTemplate restTemplate = new RestTemplate();
        List<Key> keys = new ArrayList<>(restTemplate.exchange(
                "http://localhost:3000/key",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Key>>() {
                }).getBody());
        List<Room> rooms = new ArrayList<>(restTemplate.exchange(
                "http://localhost:3000/room",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Room>>() {
                }).getBody());


        System.out.println("keys: " + keys);
        System.out.println("rooms: " + rooms);

        ArrayList<Access> result = new ArrayList<>();

        for (Key key : keys) {
            for (Room room : rooms) {
                result.add(new Access(key.id(), room.id()));
            }
        }

        return result;
    }

    private static record Key(UUID id, String owner) {
    }

    private static record Room(UUID id, String raum) {
    }

}
