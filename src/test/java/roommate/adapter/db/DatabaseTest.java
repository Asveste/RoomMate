package roommate.adapter.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import roommate.applicationservice.WorkspaceRepository;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ActiveProfiles("test")
public class DatabaseTest {

    @Autowired
    WorkspaceDao dataRepository;

        @BeforeEach
        void setup() {

        }

        @Test
        @DisplayName("Ein Workspace kann gespeichert und geladen werden")
        void test_1() {
            // Arrange
            // Act
            // Assert
        }

        @Test
        @DisplayName("Wenn es mehrere Workspaces gibt, werden alle gefunden")
        void test_2() {
            // Arrange
            // Act
            // Assert
        }

}