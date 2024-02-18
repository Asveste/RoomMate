package roommate.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Workspace {

    private final Integer id;
    private final Room number;
    private final List<Trait> traits;
    private final Set<Timespan> existingReservations;

    public Workspace(Integer id, Room number) {
        this.id = id;
        this.number = number;
        this.traits = new ArrayList<>();
        this.existingReservations = new HashSet<>();
    }

    public Workspace(Integer id, Room number, List<Trait> traits) {
        this.id = id;
        this.number = number;
        this.traits = traits;
        this.existingReservations = new HashSet<>();
    }

    public Workspace(Integer id, Room number, List<Trait> traits,
                     Set<Timespan> existingReservations) {
        this.id = id;
        this.number = number;
        this.traits = traits;
        this.existingReservations = existingReservations;
    }

    public Integer getId() {
        return id;
    }

    public Room getNumber() {
        return number;
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public Set<Timespan> getExistingReservations() {
        return existingReservations;
    }

    @Override
    public String toString() {
        return "Workspace{" +
                "id=" + id +
                ", number=" + number +
                ", traits=" + traits +
                ", existingReservations=" + existingReservations +
                '}';
    }

    public boolean isOverlap(Timespan newReservation) {
        for (Timespan existingReservation : existingReservations) {
            if (newReservation.startTime().isBefore(existingReservation.endTime())
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
}
