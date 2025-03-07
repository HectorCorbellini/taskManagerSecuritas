package com.securitas.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Location {
    private Long id;
    private String name;
    private String type;
    private String address;
    private LocalDateTime createdAt;
}
