package roommate.domain.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Workspace {

    private final List<Trait> traits = new ArrayList<>();
    private final List<Timespan> existingReservations = new ArrayList<>();
    private final Integer id;
    private final UUID room;

    public Workspace(Integer id, UUID room) {
        this.id = id;
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
}
