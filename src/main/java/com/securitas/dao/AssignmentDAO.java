package com.securitas.dao;

import com.securitas.model.Assignment;
import com.securitas.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {
    public Assignment save(Assignment assignment) throws SQLException {
        String sql = "INSERT INTO assignments (shift_id, assignment_date, is_reten, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, assignment.getShiftId());
            stmt.setDate(2, Date.valueOf(assignment.getAssignmentDate()));
            stmt.setBoolean(3, assignment.isReten());
            stmt.setString(4, assignment.getStatus());
            stmt.setString(5, assignment.getNotes());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    assignment.setId(generatedKeys.getLong(1));
                }
            }
        }
        return assignment;
    }

    public List<Assignment> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignments WHERE assignment_date BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = new Assignment();
                    assignment.setId(rs.getLong("id"));
                    assignment.setShiftId(rs.getLong("shift_id"));
                    assignment.setAssignmentDate(rs.getDate("assignment_date").toLocalDate());
                    assignment.setReten(rs.getBoolean("is_reten"));
                    assignment.setStatus(rs.getString("status"));
                    assignment.setNotes(rs.getString("notes"));
                    assignment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    assignments.add(assignment);
                }
            }
        }
        return assignments;
    }

    public Assignment findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM assignments WHERE assignment_date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Assignment assignment = new Assignment();
                    assignment.setId(rs.getLong("id"));
                    assignment.setShiftId(rs.getLong("shift_id"));
                    assignment.setAssignmentDate(rs.getDate("assignment_date").toLocalDate());
                    assignment.setReten(rs.getBoolean("is_reten"));
                    assignment.setStatus(rs.getString("status"));
                    assignment.setNotes(rs.getString("notes"));
                    assignment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return assignment;
                }
            }
        }
        return null;
    }

    public void updateAssignment(Assignment assignment) throws SQLException {
        String sql = "UPDATE assignments SET shift_id = ?, is_reten = ?, status = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, assignment.getShiftId());
            stmt.setBoolean(2, assignment.isReten());
            stmt.setString(3, assignment.getStatus());
            stmt.setString(4, assignment.getNotes());
            stmt.setLong(5, assignment.getId());
            
            stmt.executeUpdate();
        }
    }
}
