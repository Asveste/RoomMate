package roommate.applicationservice;

import org.springframework.stereotype.Service;
import roommate.domain.model.Timespan;
import roommate.domain.model.Trait;
import roommate.domain.model.Workspace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
        return repo.findAll().stream()
                .filter(workspace -> workspace.traits().containsAll(traits))
                .toList();
    }

    public Integer addWorkspace(UUID room) {
        Workspace workspace = new Workspace(room);
        Workspace savedWorkspace = repo.save(workspace);
        return savedWorkspace.id();
    }

    public void addTrait(Integer id, String trait) {
        if (id == null || trait == null || trait.isBlank()) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        workspace.addTrait(new Trait(trait));
        repo.save(workspace);
    }

    public void addTraits(Integer id, List<String> traits) {
        if (id == null || traits == null || traits.isEmpty()) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        traits.forEach(trait -> {
            try {
                addTrait(id, trait);
            } catch (InvalidInput e) {
                System.err.println("Ignored invalid trait: " + trait);
            }
        });
    }

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

    public void addRecurringReservation(Integer id, Timespan timespan) {
        if (id == null || timespan == null) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        List<Timespan> recurring = new ArrayList<>();
        recurring.add(timespan);
        for (int i = 1; i < 8; i++) {
            recurring.add(new Timespan(timespan.date().plusWeeks(i), timespan.startTime(), timespan.endTime(), timespan.timespanId()));
        }
        recurring.forEach(timespan1 -> {
            if (!workspace.isValid(timespan) || workspace.isOverlap(timespan)) {
                throw new InvalidInput();
            }
        });
        recurring.forEach(timespan1 -> {
            workspace.addReservation(timespan);
            repo.save(workspace);
        });

    }

    public void deleteWorkspace(Integer id) {
        if (id == null) {
            throw new InvalidInput();
        }
        Workspace workspace = repo.findById(id)
                .orElseThrow(NotExistentException::new);
        repo.deleteById(id);
    }

    public void removeTrait(Integer id, String trait) {
        if (id == null || trait == null || trait.isBlank()) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        Trait traitToRemove = new Trait(trait);
        workspace.removeTrait(traitToRemove);
        repo.save(workspace);
    }

    public void removeReservation(Integer id, Timespan timespan) {
        if (id == null || timespan == null) {
            throw new InvalidInput();
        }
        Workspace workspace = workspace(id);
        workspace.removeReservation(timespan);
        repo.save(workspace);
    }
}
