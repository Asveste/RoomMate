package roommate.domain.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Workspace implements Comparable<Workspace> {

    private List<Trait> traits = new ArrayList<>();
    private List<Timespan> existingReservations = new ArrayList<>();
    private Integer id;
    private final UUID room;

    public Workspace(Integer id, UUID room, List<Trait> traits, List<Timespan> existingReservations) {
        this.id = id;
        this.room = room;
        this.traits = traits;
        this.existingReservations = existingReservations;
    }

    public Workspace(Integer id, UUID room) {
        this.id = id;
        this.room = room;
    }

    public Workspace(UUID room) {
        this.room = room;
    }

    public Integer id() {
        return id;
    }

    public UUID room() {
        return room;
    }

    public List<Trait> traits() {
        return traits;
    }

    public List<Timespan> existingReservations() {
        return existingReservations;
    }

    public boolean isOverlap(Timespan newReservation) {
        for (Timespan existingReservation : existingReservations) {
            if (newReservation.date().equals(existingReservation.date())
                    && newReservation.startTime().isBefore(existingReservation.endTime())
                    && newReservation.endTime().isAfter(existingReservation.startTime())) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(Timespan newReservation) {
        if (newReservation.startTime().isAfter(newReservation.endTime())) {
            return false;
        }
        return !newReservation.startTime().equals(newReservation.endTime());
    }

    public void addTrait(Trait trait) {
        traits.add(trait);
    }

    public void addReservation(Timespan timespan) {
        existingReservations.add(timespan);
    }

    public void removeTrait(Trait traitToRemove) {
        traits.remove(traitToRemove);
    }

    public void removeReservation(Timespan timespan) {
        if (isValid(timespan) && !isOverlap(timespan)) {
            existingReservations.remove(timespan);
        }
    }

    @Override
    public String toString() {
        return "Workspace{"
                + "id=" + id
                + ", room=" + room
                + ", traits=" + traits
                + ", existingReservations=" + existingReservations
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Workspace workspace = (Workspace) o;
        return Objects.equals(id, workspace.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Workspace other) {
        return id.compareTo(other.id);
    }
}
