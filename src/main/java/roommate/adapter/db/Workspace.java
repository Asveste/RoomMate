package roommate.adapter.db;

import org.springframework.data.annotation.Id;

import java.util.Set;

public record Workspace(@Id Integer id, Integer roomId, Set<Trait> traitId,
                        Set<Timespan> existingReservations) {
}
