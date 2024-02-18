package roommate.adapter.db;

import org.springframework.data.annotation.Id;

public record TraitDto(@Id Integer id, String name) {
}
