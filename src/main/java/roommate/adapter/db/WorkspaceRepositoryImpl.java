package roommate.adapter.db;

import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;
import roommate.applicationservice.WorkspaceRepository;
import roommate.domain.model.Room;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final WorkspaceDao db;
    private final RoomDao dbr;

    public WorkspaceRepositoryImpl(WorkspaceDao db, RoomDao dbr) {
        this.db = db;
        this.dbr = dbr;
    }

    private Trait convertTrait(roommate.adapter.db.Trait trait) {
        return new Trait(trait.id(), trait.name());
    }

    private Timespan convertTimespan(roommate.adapter.db.Timespan timespan) {
        return new Timespan(timespan.date(), timespan.startTime(), timespan.endTime(),
                timespan.workspaceId());
    }

    private Room convertRoom(roommate.adapter.db.Room room) {
        return new Room(room.uuid(), room.id(), room.name());
    }

    private Workspace convertWorkspace(roommate.adapter.db.Workspace workspace) {
        Workspace result = new Workspace(workspace.id(), getRoom(workspace.roomId()));
        workspace.traitId().forEach(d -> result.addTrait(convertTrait(d)));
        workspace.existingReservations().forEach(d -> result.addReservation(convertTimespan(d)));
        return result;
    }

    private Room getRoom(Integer id) {
        Optional<roommate.adapter.db.Room> result = dbr.findById(id);
        if (result.isPresent()) {
            return convertRoom(result.get());
        }
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
