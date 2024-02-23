package roommate.adapter.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roommate.applicationservice.WorkspaceRepository;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJdbcTest
@ActiveProfiles("test")
public class DatabaseTest {

    @Autowired
    WorkspaceDao workspaceRepository;
    WorkspaceRepository repository;

    @BeforeEach
    void setup() {
        repository = new WorkspaceRepositoryImpl(workspaceRepository);
    }

    @Test
    @DisplayName("Ein Workspace kann gespeichert und geladen werden")
    void test_1() {
        // Arrange
        Workspace workspace = new Workspace(123, UUID.randomUUID());

        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 42));
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(17, 0), LocalTime.of(18, 0), 43));

        workspace.addTrait(new Trait("foo"));
        workspace.addTrait(new Trait("bar"));
        // Act
        Workspace saved = repository.save(workspace);
        Optional<Workspace> loaded = repository.findById(saved.id());
        // Assert
        assertThat(loaded.map(Workspace::existingReservations).orElseThrow()).hasSize(2);
        assertThat(loaded.map(Workspace::traits).orElseThrow()).hasSize(2);
    }

    @Test
    @DisplayName("Wenn es mehrere Workspaces gibt, werden alle gefunden")
    @Sql("findall.sql")
    void test_2() {
        // Arrange & Act
        Collection<roommate.domain.model.Workspace> all = repository.findAll();
        // Assert
        assertThat(all).hasSize(3);
    }

    @Test
    @DisplayName("Man kann eine Reservierung hinzufügen und laden")
    void test_3() {
        // Arrange
        Workspace workspace = new Workspace(123, UUID.randomUUID());
        workspace.addReservation(new Timespan(LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 4201337));
        // Act
        Workspace saved = repository.save(workspace);
        Optional<Workspace> loaded = repository.findById(saved.id());

        List<Timespan> timespanList = loaded.map(Workspace::existingReservations).orElseThrow();
        Timespan timespan = timespanList.getFirst();
        // Assert
        assertEquals(4201337, timespan.timespanId());
    }
}