package roommate.adapter.db;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public record RoomDto(UUID uuid, @Id Integer id, String name) {
}
