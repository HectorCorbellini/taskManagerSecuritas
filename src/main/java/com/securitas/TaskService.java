package com.securitas;

import com.securitas.dao.AssignmentDAO;
import com.securitas.dao.LocationDAO;
import com.securitas.dao.RestScheduleDAO;
import com.securitas.dao.ShiftDAO;
import com.securitas.model.Assignment;
import com.securitas.model.Location;
import com.securitas.model.RestSchedule;
import com.securitas.model.Shift;
import com.securitas.service.TaskManagerService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskManagerService service;

    public TaskService() {
        this.service = new TaskManagerService();
    }

    public LocalDate getDynamicDate() throws Exception {
        return java.time.LocalDate.now();
    }

    public void generateRestSchedules(LocalDate today, int weeks) throws SQLException {
        service.generateRestSchedules(today, weeks);
    }

    public void processCommand(String[] args) throws SQLException {
        service.processCommand(args);
    }

    public Location addLocation(String name, String type, String address) throws SQLException {
        return service.addLocation(name, type, address);
    }

    public Shift addShift(Long locationId, java.time.LocalTime startTime, java.time.LocalTime endTime, boolean isArmed, boolean isRecurring, String recurrencePattern) throws SQLException {
        return service.addShift(locationId, startTime, endTime, isArmed, isRecurring, recurrencePattern);
    }

    public Assignment addAssignment(Long shiftId, LocalDate assignmentDate, boolean isReten, String notes) throws SQLException {
        return service.addAssignment(shiftId, assignmentDate, isReten, notes);
    }

    public void updateAssignmentDetails(Long assignmentId, boolean isReten, String status, String notes) throws SQLException {
        service.updateAssignmentDetails(assignmentId, isReten, status, notes);
    }

    public List<Assignment> getAssignmentsForDateRange(LocalDate start, LocalDate end) throws SQLException {
        return service.getAssignmentsForDateRange(start, end);
    }

    public List<Location> getAllLocations() throws SQLException {
        return service.getAllLocations();
    }

    public List<Shift> getRecurringShifts() throws SQLException {
        return service.getRecurringShifts();
    }

    public boolean isRestDay(LocalDate date) throws SQLException {
        return service.isRestDay(date);
    }
    
    public Assignment getAssignmentForDate(LocalDate date) throws SQLException {
        return service.getAssignmentForDate(date);
    }

    // Add similar methods for adding shifts and assignments...
}
