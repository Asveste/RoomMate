package roommate.adapter.db;

import org.springframework.data.annotation.Id;

public record Trait(@Id Integer id, String name) {
}
