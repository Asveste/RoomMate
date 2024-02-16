package roommate.domain.model;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;
import java.time.LocalTime;

public record Timespan(@FutureOrPresent LocalDate date, LocalTime startTime, LocalTime endTime) {
}
