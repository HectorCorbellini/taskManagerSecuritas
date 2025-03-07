package com.securitas.model;

import lombok.Data;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

@Data
public class RestSchedule {
    private Long id;
    private LocalDate weekStartDate;
    private boolean isTwoDayRest;
    private LocalDate restDay1;
    private LocalDate restDay2;
    private LocalDateTime createdAt;

    public static LocalDate getWeekStart(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }

    public void generateRestDays() {
        if (isTwoDayRest) {
            restDay1 = weekStartDate.plusDays(3); // Wednesday
            restDay2 = weekStartDate.plusDays(4); // Thursday
        } else {
            restDay1 = weekStartDate.plusDays(4); // Thursday
            restDay2 = null;
        }
    }
}
