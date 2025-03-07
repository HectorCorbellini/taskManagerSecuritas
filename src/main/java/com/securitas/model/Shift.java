package com.securitas.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Shift {
    private Long id;
    private Long locationId;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isArmed;
    private boolean isRecurring;
    private String recurrencePattern;
    private LocalDateTime createdAt;
    
    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(14, 0);
    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(22, 0);
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime != null ? startTime : DEFAULT_START_TIME;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime != null ? endTime : DEFAULT_END_TIME;
    }
}
