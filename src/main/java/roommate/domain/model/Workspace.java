package roommate.domain.model;

import java.util.List;
import java.util.Set;

public class Workspace {

    private final Integer id;
    private final Room number;
    private final List<Trait> traits;
    private Set<Timespan> existingReservations;

    public Workspace(Integer id, Room number, List<Trait> traits) {
        this.id = id;
        this.number = number;
        this.traits = traits;
    }

    public Workspace(Integer id, Room number, List<Trait> traits, Set<Timespan> existingReservations) {
        this.id = id;
        this.number = number;
        this.traits = traits;
        this.existingReservations = existingReservations;
    }

    public boolean isOverlap(Timespan newReservation) {
        for (Timespan existingReservation : existingReservations) {
            if (newReservation.startTime().isBefore(existingReservation.endTime()) && newReservation.endTime().isAfter(existingReservation.startTime())) {
                return true;
            }
        }
        return false;
    }
}
