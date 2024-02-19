package roommate.applicationservice;

import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.util.Collection;
import java.util.List;

public class BookingService {
    private final WorkspaceRepository repo;

    public BookingService(WorkspaceRepository repo) {
        this.repo = repo;
    }

    public List<Workspace> allWorkspaces() {
        return repo.findAll().stream().sorted().toList();
    }

    public Workspace workspace(Integer id) {
        return repo.findById(id)
                .orElseThrow(NotExistentException::new);
    }

    public List<Workspace> allWorkspacesByTraits(Collection<Trait> traits) {
        return repo.findAll().stream()
                .filter(workspace -> workspace.traits().containsAll(traits))
                .toList();
    }
}
