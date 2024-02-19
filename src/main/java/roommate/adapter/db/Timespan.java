package roommate.adapter.db;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalTime;

public record Timespan(LocalDate date, LocalTime startTime, LocalTime endTime, @Id Integer timespanId) {
}
