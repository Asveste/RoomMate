package roommate.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkspaceTest {
    @Test
    @DisplayName("Wenn bei Timespan die startTime nach der endTime ist, wird false zurückgegeben")
    void isValid() {
        Workspace workspace = new Workspace(0, UUID.randomUUID());
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(16, 0), LocalTime.of(15, 59), 42));

        List<Timespan> existingReservations = workspace.existingReservations();
        Timespan timespan = existingReservations.stream().findFirst()
                .orElse(new Timespan(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), 42));

        boolean valid = workspace.isValid(timespan);

        assertFalse(valid);
    }

    @Test
    @DisplayName("Wenn bei Timespan die endTime == startTime ist, wird false zurückgegeben")
    void isValid2() {
        Workspace workspace = new Workspace(0, UUID.randomUUID());
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(14, 0), 43));

        List<Timespan> existingReservations = workspace.existingReservations();
        Timespan timespan = existingReservations.stream().findFirst()
                .orElse(new Timespan(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), 43));

        boolean valid = workspace.isValid(timespan);

        assertFalse(valid);
    }

    @Test
    @DisplayName("Wenn sich Zeiten überlappen wird true zurückgegeben")
    void isOverlap() {
        Workspace workspace = new Workspace(0, UUID.randomUUID());
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 44));

        boolean valid = workspace.isOverlap(new Timespan(LocalDate.now(), LocalTime.of(15, 0), LocalTime.of(17, 0), 44));

        assertTrue(valid);
    }

    @Test
    @DisplayName("Wenn sich Zeiten nicht überlappen wird false zurückgegeben")
    void isOverlap2() {
        Workspace workspace = new Workspace(0, UUID.randomUUID());
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 45));

        boolean valid = workspace.isOverlap(new Timespan(LocalDate.now(), LocalTime.of(16, 1), LocalTime.of(17, 0), 45));

        assertFalse(valid);
    }

    @Test
    @DisplayName("Wenn Zeiten gleich sind aber das Datum unterschiedlich wird false zurückgegeben")
    void isOverlap3() {
        Workspace workspace = new Workspace(0, UUID.randomUUID());
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 46));

        boolean valid = workspace.isOverlap(new Timespan(LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(16, 0), 46));

        assertFalse(valid);
    }
}