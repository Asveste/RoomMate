package roommate.applicationservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookingService {
    private final WorkspaceRepository repo;

    public BookingService(WorkspaceRepository repo) {
        this.repo = repo;
    }

    public List<Workspace> allWorkspaces() {
        return repo.findAll().stream().sorted().toList();
    }

    public Workspace workspace(Integer id) {
        return repo.findById(id)
                .orElseThrow(NotExistentException::new);
    }

    public List<Workspace> filterWorkspacesByTraits(List<Workspace> workspaces, List<Trait> traits) {
        if (traits == null || traits.isEmpty()) {
            return workspaces;
        }
        return workspaces.stream()
                .filter(workspace -> workspace.traits().containsAll(traits))
                .collect(Collectors.toList());
    }

    public List<Workspace> filterWorkspacesByTimespan(List<Workspace> workspaces, Timespan timespan) {
        if (timespan == null || (timespan.date() == null && timespan.startTime() == null && timespan.endTime() == null)) {
            return workspaces;
        }
        return workspaces.stream()
                .filter(workspace -> !workspace.isOverlap(timespan))
                .collect(Collectors.toList());
    }

    public List<Trait> allTraitsFromWorkspaces(List<Workspace> workspaces) {
        if (workspaces == null) {
            return Collections.emptyList();
        }
        return workspaces.stream()
                .filter(Objects::nonNull)
                .flatMap(workspace -> workspace.traits() == null
                        ? Stream.empty() : workspace.traits().stream())
                .distinct()
                .sorted(Comparator.comparing(Trait::trait))
                .toList();
    }

    @Transactional
    public void addReservation(Integer id, Timespan timespan) {
        if (id == null || timespan == null) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        if (workspace.isValid(timespan) && !workspace.isOverlap(timespan)) {
            workspace.addReservation(timespan);
            repo.save(workspace);
        }
    }

    @Transactional
    public void addRecurringReservation(Integer id, Timespan timespan) {
        if (id == null || timespan == null) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        List<Timespan> recurring = new ArrayList<>();
        recurring.add(timespan);
        for (int i = 1; i < 8; i++) {
            recurring.add(new Timespan(timespan.date().plusWeeks(i), timespan.startTime(),
                    timespan.endTime(), timespan.timespanId()));
        }
        recurring.forEach(timespan1 -> {
            if (!workspace.isValid(timespan1) || workspace.isOverlap(timespan1)) {
                throw new InvalidInput();
            }
        });
        recurring.forEach(workspace::addReservation);
        repo.save(workspace);
    }

    public Integer addWorkspaceAdmin(UUID room) {
        Workspace workspace = new Workspace(room);
        Workspace savedWorkspace = repo.save(workspace);
        return savedWorkspace.id();
    }

    public void deleteWorkspaceAdmin(Integer id) {
        if (id == null) {
            throw new InvalidInput();
        }
        Workspace workspace = repo.findById(id)
                .orElseThrow(NotExistentException::new);
        repo.deleteById(id);
    }

    @Transactional
    public void addTraitAdmin(Integer id, String trait) {
        if (id == null || trait == null || trait.isBlank()) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        workspace.addTrait(new Trait(trait));
        repo.save(workspace);
    }

    @Transactional
    public void deleteTraitAdmin(Integer id, String trait) {
        if (id == null || trait == null || trait.isBlank()) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        Trait traitToRemove = new Trait(trait);
        workspace.removeTrait(traitToRemove);
        repo.save(workspace);
    }

    @Transactional
    public void lockWorkspaceAdmin(Integer id, Timespan timespan) {
        Workspace workspace = workspace(id);
        workspace.overwriteReservation(timespan);
        repo.save(workspace);
    }

    @Transactional
    public void cancelReservationAdmin(Integer id, Timespan timespan) {
        if (id == null || timespan == null) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        workspace.forceRemoveReservation(timespan);
        repo.save(workspace);
    }

    @Transactional
    public void overwriteReservationsAdmin(Integer id, Timespan timespan) {
        if (id == null || timespan == null) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        workspace.overwriteReservation(timespan);
        repo.save(workspace);
    }
}
