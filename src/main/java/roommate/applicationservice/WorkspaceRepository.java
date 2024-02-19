package roommate.applicationservice;

import roommate.domain.model.Workspace;

import java.util.Collection;
import java.util.Optional;

public interface WorkspaceRepository {
    Workspace save(Workspace workspace);

    void deleteById(Integer id);

    Optional<Workspace> findById(Integer id);

    Collection<Workspace> findAll();
}
