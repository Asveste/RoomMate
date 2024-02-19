package roommate.adapter.db;

import org.springframework.stereotype.Repository;
import roommate.applicationservice.WorkspaceRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final WorkspaceDao db;

    public WorkspaceRepositoryImpl(WorkspaceDao db) {
        this.db = db;
    }

//    private Room convertRoom(roommate.adapter.db.Room room) {
//        return new Room(room.uuid(), room.id(), room.name());
//    }

    private roommate.domain.model.Trait convertTrait(Trait trait) {
        return new roommate.domain.model.Trait(trait.trait());
    }

    private roommate.domain.model.Timespan convertTimespan(Timespan timespan) {
        return new roommate.domain.model.Timespan(timespan.date(), timespan.startTime(),
                timespan.endTime());
    }

    private roommate.domain.model.Workspace convertWorkspace(Workspace workspace) {
        roommate.domain.model.Workspace result =
                new roommate.domain.model.Workspace(workspace.id(), workspace.room());
        workspace.traits().forEach(d -> result.addTrait(convertTrait(d)));
        workspace.existingReservations().forEach(d -> result.addReservation(convertTimespan(d)));
        return result;
    }

//    private Room getRoom(Integer id) {
//        Optional<roommate.adapter.db.Room> result = dbr.findById(id);
//        if (result.isPresent()) {
//            return convertRoom(result.get());
//        }
//        return null;
//    }

    @Override
    public roommate.domain.model.Workspace save(roommate.domain.model.Workspace workspace) {
        System.out.println("Davor");
        Integer id = db.findById(workspace.id())
                .map(roommate.adapter.db.Workspace::id)
                .orElse(null);
        List<roommate.adapter.db.Trait> traits = workspace.traits().stream()
                .map(this::extractTrait)
                .toList();
        List<roommate.adapter.db.Timespan> existingReservations = workspace.existingReservations()
                .stream()
                .map(this::extractTime)
                .toList();
        roommate.adapter.db.Workspace obj =
                new roommate.adapter.db.Workspace(id, workspace.room(), traits,
                        existingReservations);
        roommate.adapter.db.Workspace save = db.save(obj);
        return convertWorkspace(save);
    }

    private Trait extractTrait(roommate.domain.model.Trait trait) {
        return new Trait(trait.trait());
    }

    private Timespan extractTime(roommate.domain.model.Timespan timespan) {
        return new Timespan(timespan.date(), timespan.startTime(),
                timespan.endTime());
    }

    @Override
    public void deleteById(Integer id) {
        db.deleteById(id);
    }

    @Override
    public Optional<roommate.domain.model.Workspace> findById(Integer id) {
        return db.findById(id).map(this::convertWorkspace);
    }

    @Override
    public Collection<roommate.domain.model.Workspace> findAll() {
        Collection<roommate.adapter.db.Workspace> workspace = db.findAll();
        return workspace.stream().map(this::convertWorkspace).toList();
    }
}
