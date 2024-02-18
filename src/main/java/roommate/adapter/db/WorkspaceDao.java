package roommate.adapter.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceDao extends CrudRepository<WorkspaceDto, Integer> {
    Integer save(WorkspaceDto workspaceDto);

    void deleteById(Integer id);

    Optional<WorkspaceDto> findById(Integer id);

    List<WorkspaceDto> findAll();
}
