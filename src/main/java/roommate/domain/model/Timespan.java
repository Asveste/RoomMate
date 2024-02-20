package roommate.domain.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import roommate.domain.validation.ValidTimespan;
import roommate.domain.validation.onPost;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidTimespan
public record Timespan(@FutureOrPresent LocalDate date, @NotNull(groups = onPost.class) LocalTime startTime,
                       @NotNull(groups = onPost.class) LocalTime endTime, Integer timespanId) {
}