package com.securitas.service;

import com.securitas.dao.AssignmentDAO;
import com.securitas.dao.LocationDAO;
import com.securitas.dao.RestScheduleDAO;
import com.securitas.dao.ShiftDAO;
import com.securitas.model.Assignment;
import com.securitas.model.Location;
import com.securitas.model.RestSchedule;
import com.securitas.model.Shift;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TaskManagerService {
    private final LocationDAO locationDAO;
    private final ShiftDAO shiftDAO;
    private final AssignmentDAO assignmentDAO;
    private final RestScheduleDAO restScheduleDAO;
    
    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(14, 0);
    private static final LocalTime DEFAULT_END_TIME = LocalTime.of(22, 0);

    public TaskManagerService() {
        this.locationDAO = new LocationDAO();
        this.shiftDAO = new ShiftDAO();
        this.assignmentDAO = new AssignmentDAO();
        this.restScheduleDAO = new RestScheduleDAO();
    }

    public Location addLocation(String name, String type, String address) throws SQLException {
        Location location = new Location();
        location.setName(name);
        location.setType(type);
        location.setAddress(address);
        return locationDAO.save(location);
    }

    public Shift addShift(Long locationId, LocalTime startTime, LocalTime endTime, 
                         boolean isArmed, boolean isRecurring, String recurrencePattern) throws SQLException {
        Shift shift = new Shift();
        shift.setLocationId(locationId);
        shift.setStartTime(startTime != null ? startTime : DEFAULT_START_TIME);
        shift.setEndTime(endTime != null ? endTime : DEFAULT_END_TIME);
        shift.setArmed(isArmed);
        shift.setRecurring(isRecurring);
        shift.setRecurrencePattern(recurrencePattern);
        return shiftDAO.save(shift);
    }

    public Assignment addAssignment(Long shiftId, LocalDate date, boolean isReten, String notes) throws SQLException {
        // Validate that the assignment is not on a rest day
        validateAssignment(date);
        
        Assignment assignment = new Assignment();
        assignment.setShiftId(shiftId);
        assignment.setAssignmentDate(date);
        assignment.setReten(isReten);
        assignment.setStatus("SCHEDULED");
        assignment.setNotes(notes);
        return assignmentDAO.save(assignment);
    }

    public Assignment getAssignmentForDate(LocalDate date) throws SQLException {
        return assignmentDAO.findByDate(date);
    }

    public List<Assignment> getAssignmentsForDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        return assignmentDAO.findByDateRange(startDate, endDate);
    }

    public void updateAssignmentDetails(Long assignmentId, boolean isReten, String status, String notes) throws SQLException {
        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);
        assignment.setReten(isReten);
        assignment.setStatus(status);
        assignment.setNotes(notes);
        assignmentDAO.updateAssignment(assignment);
    }

    public List<Location> getAllLocations() throws SQLException {
        return locationDAO.findAll();
    }

    public List<Shift> getRecurringShifts() throws SQLException {
        return shiftDAO.findRecurringShifts();
    }

    public List<Shift> getShiftsForLocation(Long locationId) throws SQLException {
        return shiftDAO.findByLocationId(locationId);
    }
    
    // Rest schedule methods
    public void generateRestSchedules(LocalDate startDate, int weeks) throws SQLException {
        LocalDate weekStart = RestSchedule.getWeekStart(startDate);
        boolean isTwoDayRest = true; // Start with 2-day rest week
        
        for (int i = 0; i < weeks; i++) {
            RestSchedule schedule = new RestSchedule();
            schedule.setWeekStartDate(weekStart);
            schedule.setTwoDayRest(isTwoDayRest);
            schedule.generateRestDays();
            
            restScheduleDAO.save(schedule);
            
            weekStart = weekStart.plusWeeks(1);
            isTwoDayRest = !isTwoDayRest;
        }
    }
    
    public boolean isRestDay(LocalDate date) throws SQLException {
        return restScheduleDAO.isRestDay(date);
    }
    
    private void validateAssignment(LocalDate date) throws SQLException {
        if (isRestDay(date)) {
            throw new IllegalArgumentException("Cannot create assignment on rest day: " + date);
        }
    }
}
