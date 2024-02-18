package roommate.adapter.db;

import org.springframework.stereotype.Repository;
import roommate.applicationservice.WorkspaceRepository;
import roommate.domain.model.Room;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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
        Integer id = db.findById(workspace.getId())
                .map(roommate.adapter.db.Workspace::id)
                .orElse(null);
        Set<roommate.adapter.db.Trait> traits = workspace.getTraits().stream()
                .map(this::extractTrait)
                .collect(Collectors.toSet());
        Set<roommate.adapter.db.Timespan> times = workspace.getExistingReservations().stream()
                .map(this::extractTime)
                .collect(Collectors.toSet());
        roommate.adapter.db.Workspace obj =
                new roommate.adapter.db.Workspace(id, workspace.getRoomId(), traits, times);
        roommate.adapter.db.Workspace save = db.save(obj);
        return convertWorkspace(save);
    }

    private roommate.adapter.db.Trait extractTrait(Trait trait) {
        return new roommate.adapter.db.Trait(trait.getId(), trait.getTrait());
    }

    private roommate.adapter.db.Timespan extractTime(Timespan timespan) {
        return new roommate.adapter.db.Timespan(timespan.date(), timespan.startTime(),
                timespan.endTime(), timespan.workspaceId());
    }

    @Override
    public void deleteById(Integer id) {
        db.deleteById(id);
    }

    @Override
    public Optional<Workspace> findById(Integer id) {
        return db.findById(id).map(this::convertWorkspace);
    }

    @Override
    public Set<Workspace> findAll() {
        Collection<roommate.adapter.db.Workspace> workspace = db.findAll();
        return workspace.stream().map(this::convertWorkspace).collect(Collectors.toSet());
    }
}
