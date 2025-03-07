package com.securitas.model;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Assignment {
    private Long id;
    private Long shiftId;
    private LocalDate assignmentDate;
    private boolean isReten;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
}
