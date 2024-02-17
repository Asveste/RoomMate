package roommate.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class WorkspaceTest {
    @Test
    @DisplayName("Wenn sich zwei Zeiten überlappen wird true zurückgegeben")
    void isOverlap() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(0, room, List.of(), Set.of(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(16, 0), 1)));

        boolean overlap = workspace.isOverlap(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(15, 0), LocalTime.of(17, 0), 1));

        assertTrue(overlap);
    }

    @Test
    @DisplayName("Wenn sich zwei Zeiten nicht überlappen wird false zurückgegeben")
    void isOverlap2() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(0, room, List.of(), Set.of(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(15, 0), 1)));

        boolean overlap = workspace.isOverlap(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(15, 0), LocalTime.of(17, 0), 1));

        assertFalse(overlap);
    }
}