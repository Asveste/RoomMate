package roommate.adapter.db;

import org.springframework.stereotype.Repository;
import roommate.applicationservice.WorkspaceRepository;
import roommate.domain.model.Workspace;

import java.util.Optional;
import java.util.Set;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final WorkspaceDao db;

    public WorkspaceRepositoryImpl(WorkspaceDao db) {
        this.db = db;
    }

    private Workspace convertWorkspace(WorkspaceDao workspaceDao) {
        return null;
    }
    @Override
    public Workspace save(Workspace workspace) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        db.deleteById(id);
    }

    @Override
    public Optional<Workspace> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Set<Workspace> findAll() {
        return null;
    }
}
