package roommate.adapter.db;

import org.springframework.stereotype.Repository;
import roommate.domain.applicationservice.WorkspaceRepository;
import roommate.domain.model.Workspace;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final WorkspaceDao db;

    public WorkspaceRepositoryImpl(WorkspaceDao db) {
        this.db = db;
    }

    @Override
    public Integer save(Workspace workspace) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Optional<Workspace> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Workspace> findAll() {
        return null;
    }
}
