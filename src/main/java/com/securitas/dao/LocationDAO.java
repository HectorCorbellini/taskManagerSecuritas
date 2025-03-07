package com.securitas.dao;

import com.securitas.model.Location;
import com.securitas.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    public Location save(Location location) throws SQLException {
        String sql = "INSERT INTO locations (name, type, address) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, location.getName());
            stmt.setString(2, location.getType());
            stmt.setString(3, location.getAddress());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    location.setId(generatedKeys.getLong(1));
                }
            }
        }
        return location;
    }

    public Location findById(Long id) throws SQLException {
        String sql = "SELECT * FROM locations WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Location location = new Location();
                    location.setId(rs.getLong("id"));
                    location.setName(rs.getString("name"));
                    location.setType(rs.getString("type"));
                    location.setAddress(rs.getString("address"));
                    location.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return location;
                }
            }
        }
        return null;
    }

    public List<Location> findAll() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Location location = new Location();
                location.setId(rs.getLong("id"));
                location.setName(rs.getString("name"));
                location.setType(rs.getString("type"));
                location.setAddress(rs.getString("address"));
                location.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                locations.add(location);
            }
        }
        return locations;
    }
}
