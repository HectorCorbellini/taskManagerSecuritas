package com.securitas.dao;

import com.securitas.model.RestSchedule;
import com.securitas.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;

public class RestScheduleDAO {
    public void save(RestSchedule schedule) throws SQLException {
        String sql = "INSERT INTO rest_schedules (week_start_date, is_two_day_rest, rest_day1, rest_day2) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(schedule.getWeekStartDate()));
            stmt.setBoolean(2, schedule.isTwoDayRest());
            stmt.setDate(3, Date.valueOf(schedule.getRestDay1()));
            stmt.setDate(4, schedule.getRestDay2() != null ? Date.valueOf(schedule.getRestDay2()) : null);
            stmt.executeUpdate();
        }
    }

    public boolean isRestDay(LocalDate date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rest_schedules WHERE ? IN (rest_day1, rest_day2)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public RestSchedule findByWeek(LocalDate date) throws SQLException {
        LocalDate weekStart = RestSchedule.getWeekStart(date);
        String sql = "SELECT * FROM rest_schedules WHERE week_start_date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(weekStart));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                RestSchedule schedule = new RestSchedule();
                schedule.setId(rs.getLong("id"));
                schedule.setWeekStartDate(rs.getDate("week_start_date").toLocalDate());
                schedule.setTwoDayRest(rs.getBoolean("is_two_day_rest"));
                schedule.setRestDay1(rs.getDate("rest_day1").toLocalDate());
                Date restDay2 = rs.getDate("rest_day2");
                schedule.setRestDay2(restDay2 != null ? restDay2.toLocalDate() : null);
                return schedule;
            }
            return null;
        }
    }
}
