package roommate.adapter.db;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

public record WorkspaceDto(@Id Integer id, Integer roomId, List<TraitDto> traitId,
                           Set<TimespanDto> existingReservations) {
}
