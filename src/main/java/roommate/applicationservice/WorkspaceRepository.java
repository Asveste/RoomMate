package roommate.applicationservice;

import roommate.domain.model.Workspace;

import java.util.Optional;
import java.util.Set;

public interface WorkspaceRepository {
    Workspace save(Workspace workspace);

    void deleteById(Integer id);

    Optional<Workspace> findById(Integer id);

    Set<Workspace> findAll();
}
