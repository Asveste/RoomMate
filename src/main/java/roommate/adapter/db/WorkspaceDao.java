package roommate.adapter.db;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface WorkspaceDao extends CrudRepository<Workspace, Integer> {
    Workspace save(Workspace workspace);

    void deleteById(Integer id);

    Optional<Workspace> findById(Integer id);

    Collection<Workspace> findAll();
}
