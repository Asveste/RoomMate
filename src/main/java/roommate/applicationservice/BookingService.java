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

    public List<Workspace> allWorkspacesByTraits(Collection<Trait> traits) {
        if (traits == null || traits.isEmpty()) {
            // Entscheide, wie du mit diesen Fällen umgehen möchtest:
            // - Für `null`, könntest du eine leere Liste zurückgeben oder eine Exception werfen.
            // - Für eine leere Liste könntest du alle Workspaces zurückgeben oder auch eine leere Liste.
            return Collections.emptyList(); // oder `return repo.findAll();` für den Fall einer leeren Liste
        }
        return repo.findAll().stream()
                .filter(workspace -> workspace.traits() != null && workspace.traits().containsAll(traits))
                .toList();
    }

//    public List<Workspace> allWorkspacesByTimespan(Collection<Timespan> timespans) {
//        return repo.findAll().stream()
//                .filter(workspace -> workspace.existingReservations().containsAll(timespans))
//                .toList();
//    }

    public List<Workspace> filterWorkspacesByTraits(List<Workspace> workspaces, List<Trait> traits) {
        if (traits == null || traits.isEmpty()) {
            return workspaces; // Keine Filterung, wenn keine Traits angegeben sind
        }
        return workspaces.stream()
                .filter(workspace -> workspace.traits().containsAll(traits))
                .collect(Collectors.toList());
    }

    public List<Workspace> filterWorkspacesByTimespan(List<Workspace> workspaces, Timespan timespan) {
        if (timespan == null || (timespan.date() == null && timespan.startTime() == null && timespan.endTime() == null)) {
            return workspaces; // Keine Filterung, wenn kein gültiger Timespan angegeben ist
        }
        return workspaces.stream()
                .filter(workspace -> !workspace.isOverlap(timespan))
                .collect(Collectors.toList());
    }

    public List<Workspace> allWorkspacesWithoutOverlap(Timespan timespan) {
        if (timespan == null) {
            // Entscheide, wie mit null Timespan umgegangen werden soll: Alle Workspaces zurückgeben oder leere Liste
            return Collections.emptyList(); // oder `return repo.findAll();` wenn Workspaces bei null Timespan zurückgegeben werden sollen
        }

        return repo.findAll().stream()
                // Filtert Workspaces heraus, die keine Überschneidung mit dem gegebenen Timespan haben
                .filter(workspace -> !workspace.isOverlap(timespan))
                .toList();
    }


    public List<Workspace> allWorkspacesByTimespans(Collection<Timespan> timespans) {
        if (timespans == null || timespans.isEmpty()) {
            return Collections.emptyList();
        }

        return repo.findAll().stream()
                .filter(workspace -> {
                    // Sicherstellen, dass existingReservations nicht null ist
                    Collection<Timespan> existingReservations = workspace.existingReservations();
                    if (existingReservations == null) {
                        return false;
                    }

                    // Für jeden Timespan in timespans überprüfen, ob mindestens eine Übereinstimmung in existingReservations existiert
                    return timespans.stream().allMatch(timespan ->
                            existingReservations.stream().anyMatch(existingReservation ->
                                    // Überprüfen auf Übereinstimmung unter Berücksichtigung möglicher null-Werte in date, startTime, endTime
                                    (timespan.date() == null || timespan.date().equals(existingReservation.date())) &&
                                            (timespan.startTime() == null || timespan.startTime().equals(existingReservation.startTime())) &&
                                            (timespan.endTime() == null || timespan.endTime().equals(existingReservation.endTime()))
                            )
                    );
                })
                .toList();
    }

    public List<Trait> allTraitsFromWorkspace(Integer id) {
        Workspace workspace = repo.findById(id)
                .orElseThrow(NotExistentException::new);
        return workspace.traits();
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

    public List<Timespan> allReservationsFromWorkspace(Integer id) {
        Workspace workspace = repo.findById(id)
                .orElseThrow(NotExistentException::new);
        return workspace.existingReservations();
    }

    public List<Timespan> allReservationsFromWorkspaces(List<Workspace> workspaces) {
        if (workspaces == null) {
            return Collections.emptyList();
        }
        return workspaces.stream()
                .filter(Objects::nonNull)
                .flatMap(workspace -> workspace.existingReservations() != null
                        ? workspace.existingReservations().stream() : Stream.empty())
                .filter(Objects::nonNull)
                .toList();
    }

    public Integer addWorkspace(UUID room) {
        Workspace workspace = new Workspace(room);
        Workspace savedWorkspace = repo.save(workspace);
        return savedWorkspace.id();
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
    public void addTraits(Integer id, List<String> traits) {
        if (id == null || traits == null || traits.isEmpty()) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        traits.forEach(trait -> {
            try {
                addTraitAdmin(id, trait);
            } catch (InvalidInput e) {
                System.err.println("Ignored invalid trait: " + trait);
            }
        });
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

    public void deleteWorkspaceAdmin(Integer id) {
        if (id == null) {
            throw new InvalidInput();
        }
        Workspace workspace = repo.findById(id)
                .orElseThrow(NotExistentException::new);
        repo.deleteById(id);
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
