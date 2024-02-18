package roommate.adapter.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roommate.applicationservice.WorkspaceRepository;
import roommate.domain.model.Room;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ActiveProfiles("test")
public class DatabaseTest {

    @Autowired
    WorkspaceDao workspaceRepository;
    RoomDao roomRepository;
    WorkspaceRepository repository;
        @BeforeEach
        void setup() {
            repository = new WorkspaceRepositoryImpl(workspaceRepository, roomRepository);
        }
        @Test
        @DisplayName("Ein Workspace kann gespeichert und geladen werden")
        void test_1() {
            // Arrange
            UUID uuid = UUID.randomUUID();
            Room room = new Room(uuid,1,"foo");
            Trait trait = new Trait(2,"bar");
            roommate.domain.model.Workspace workspace = new roommate.domain.model.Workspace(123, room);
            workspace.addTrait(trait);
            // Act
            Workspace saved = repository.save(workspace);
            Optional<roommate.domain.model.Workspace> geladen = repository.findById(saved.getId());
            // Assert
            assertThat(geladen.map(Workspace::getId).orElseThrow()).isEqualTo(123);
            assertThat(geladen.map(Workspace::getRoom).orElseThrow()).isEqualTo(room);
            //Set<Trait> loadedTraits = geladen.map(roommate.domain.model.Workspace::getTraits).orElseThrow();
            //assertThat(loadedTraits.stream().findFirst()).isEqualTo(trait);
        }
        @Disabled
        @Test
        @DisplayName("Wenn es mehrere Workspaces gibt, werden alle gefunden")
        @Sql("findall.sql")
        void test_2() {
            // Arrange
            Collection<roommate.domain.model.Workspace> all = repository.findAll();
            // Act

            // Assert
        }


}