package roommate.domain.applicationservice;

import roommate.domain.model.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {
    Integer save(Workspace workspace);

    void deleteById(Integer id);

    Optional<Workspace> findById(Integer id);

    List<Workspace> findAll();
}
