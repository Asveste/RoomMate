package roommate.adapter.db;

import java.time.LocalDate;
import java.time.LocalTime;

public record Timespan(LocalDate date, LocalTime startTime, LocalTime endTime,
                       Integer workspaceId) {
}
