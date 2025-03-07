package com.securitas.dao;

import com.securitas.model.Shift;
import com.securitas.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAO {
    public Shift save(Shift shift) throws SQLException {
        String sql = "INSERT INTO shifts (location_id, start_time, end_time, is_armed, is_recurring, recurrence_pattern) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, shift.getLocationId());
            stmt.setTime(2, Time.valueOf(shift.getStartTime()));
            stmt.setTime(3, Time.valueOf(shift.getEndTime()));
            stmt.setBoolean(4, shift.isArmed());
            stmt.setBoolean(5, shift.isRecurring());
            stmt.setString(6, shift.getRecurrencePattern());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shift.setId(generatedKeys.getLong(1));
                }
            }
        }
        return shift;
    }

    public List<Shift> findByLocationId(Long locationId) throws SQLException {
        List<Shift> shifts = new ArrayList<>();
        String sql = "SELECT * FROM shifts WHERE location_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, locationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Shift shift = new Shift();
                    shift.setId(rs.getLong("id"));
                    shift.setLocationId(rs.getLong("location_id"));
                    shift.setStartTime(rs.getTime("start_time").toLocalTime());
                    shift.setEndTime(rs.getTime("end_time").toLocalTime());
                    shift.setArmed(rs.getBoolean("is_armed"));
                    shift.setRecurring(rs.getBoolean("is_recurring"));
                    shift.setRecurrencePattern(rs.getString("recurrence_pattern"));
                    shift.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    shifts.add(shift);
                }
            }
        }
        return shifts;
    }

    public List<Shift> findRecurringShifts() throws SQLException {
        List<Shift> shifts = new ArrayList<>();
        String sql = "SELECT * FROM shifts WHERE is_recurring = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Shift shift = new Shift();
                shift.setId(rs.getLong("id"));
                shift.setLocationId(rs.getLong("location_id"));
                shift.setStartTime(rs.getTime("start_time").toLocalTime());
                shift.setEndTime(rs.getTime("end_time").toLocalTime());
                shift.setArmed(rs.getBoolean("is_armed"));
                shift.setRecurring(rs.getBoolean("is_recurring"));
                shift.setRecurrencePattern(rs.getString("recurrence_pattern"));
                shift.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                shifts.add(shift);
            }
        }
        return shifts;
    }
}
