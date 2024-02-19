package roommate.applicationservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ServiceTests {

    WorkspaceRepository repo = mock(WorkspaceRepository.class);

    Integer id = 42;
    UUID room = UUID.randomUUID();

    @Test
    @DisplayName("Ein Workspace kann hinzugefügt werden")
    void test_1() {

        BookingService service = new BookingService(repo);

        when(repo.save(any())).thenReturn(new Workspace(id, UUID.randomUUID()));

        Integer returnedId = service.addWorkspace(room);

        ArgumentCaptor<Workspace> captor = ArgumentCaptor.forClass(Workspace.class);
        verify(repo).save(captor.capture());
        Workspace savedWorkspace = captor.getValue();

        assertThat(savedWorkspace.room()).isEqualTo(room);
        assertThat(returnedId).isEqualTo(id);
    }

    @Test
    @DisplayName("Traits können hinzugefügt werden")
    void test_2() {

        Workspace workspace = new Workspace(id, UUID.randomUUID());
        when(repo.findById(any())).thenReturn(Optional.of(workspace));
        BookingService service = new BookingService(repo);
        service.addTrait(id, "trait1");

        ArgumentCaptor<Workspace> captor = ArgumentCaptor.forClass(Workspace.class);
        verify(repo).save(captor.capture());
        Workspace savedBegriff = captor.getValue();

        assertThat(savedBegriff.traits()).contains(new Trait("trait1"));
    }

    @Test
    @DisplayName("Die Workspaces sind sortiert")
    void test_3() {

        when(repo.findAll()).thenReturn(List.of(
                new Workspace(33, room),
                new Workspace(800, room),
                new Workspace(32, room)));
        BookingService service = new BookingService(repo);

        assertThat(service.allWorkspaces().stream().map(Workspace::id).toList())
                .containsExactly(32, 33, 800);
    }

    @Test
    @DisplayName("Nicht vorhandene Workspaces werfen eine Exception")
    void test_4() {
        when(repo.findById(id)).thenReturn(Optional.empty());
        BookingService service = new BookingService(repo);
        assertThat(service.allWorkspaces()).isEmpty();
        assertThrows(NotExistentException.class, () -> service.workspace(id));
    }
}
