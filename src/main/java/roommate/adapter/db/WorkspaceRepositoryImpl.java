package roommate.adapter.db;

import org.springframework.stereotype.Repository;
import roommate.applicationservice.WorkspaceRepository;
import roommate.domain.model.Room;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.util.Optional;
import java.util.Set;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final WorkspaceDao db;

    public WorkspaceRepositoryImpl(WorkspaceDao db) {
        this.db = db;
    }

    private Trait convertTrait(roommate.adapter.db.Trait trait) {
        return new Trait(trait.id(), trait.name());
    }

    private Timespan convertTimespan(roommate.adapter.db.Timespan timespan) {
        return new Timespan(timespan.date(), timespan.startTime(), timespan.endTime(), timespan.workspaceId());
    }

    private Room convertRoom(roommate.adapter.db.Room room) {
        return new Room(room.uuid(), room.id(), room.name());
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
