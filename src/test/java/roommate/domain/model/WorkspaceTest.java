package roommate.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class WorkspaceTest {
    @Test
    @DisplayName("Wenn bei Timespan die startTime nach der endTime ist, wird false zurückgegeben")
    void isValid() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(0, room, Set.of(), Set.of(new Timespan(LocalDate.now(), LocalTime.of(16, 0), LocalTime.of(15, 59), 0)));
        Set<Timespan> existingReservations = workspace.getExistingReservations();
        Timespan timespan = existingReservations.stream().findFirst().orElse(new Timespan(LocalDate.now(), LocalTime.of(16, 0), LocalTime.of(15, 0), 0));

        boolean valid = workspace.isValid(timespan);

        assertFalse(valid);
    }

    @Test
    @DisplayName("Wenn bei Timespan die endTime == startTime ist, wird false zurückgegeben")
    void isValid2() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(1, room, Set.of(), Set.of(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(14, 0), 1)));
        Set<Timespan> existingReservations = workspace.getExistingReservations();
        Timespan timespan = existingReservations.stream().findFirst().orElse(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(14, 0), 1));

        boolean valid = workspace.isValid(timespan);

        assertFalse(valid);
    }

    @Test
    @DisplayName("Wenn sich zwei Zeiten überlappen wird true zurückgegeben")
    void isOverlap() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(2, room, Set.of(), Set.of(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(16, 0), 2)));

        boolean overlap = workspace.isOverlap(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(15, 0), LocalTime.of(17, 0), 2));

        assertTrue(overlap);
    }

    @Test
    @DisplayName("Wenn sich zwei Zeiten nicht überlappen wird false zurückgegeben")
    void isOverlap2() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(3, room, Set.of(), Set.of(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(15, 0), 3)));

        boolean overlap = workspace.isOverlap(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(15, 0), LocalTime.of(17, 0), 3));

        assertFalse(overlap);
    }

    @Test
    @DisplayName("Wenn zwei Zeiten gleich sind aber das Datum unterschiedlich wird false zurückgegeben")
    void isOverlap3() {
        Room room = mock(Room.class);
        Workspace workspace = new Workspace(4, room, Set.of(), Set.of(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(15, 0), 4)));

        boolean overlap = workspace.isOverlap(new Timespan(LocalDate.now().plusDays(2), LocalTime.of(14, 0), LocalTime.of(15, 0), 4));

        assertFalse(overlap);
    }
}