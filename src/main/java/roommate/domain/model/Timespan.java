package roommate.domain.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import roommate.domain.validation.ValidTimespan;
import roommate.domain.validation.onPost;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ValidTimespan
public record Timespan(@FutureOrPresent @NotNull(groups = onPost.class) LocalDate date,
                       @NotNull(groups = onPost.class) LocalTime startTime,
                       @NotNull(groups = onPost.class) LocalTime endTime, Integer timespanId) {
    @Override
    public String toString() {
        return String.format("%s: %s Uhr - %s Uhr", date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                startTime, endTime);
    }
}