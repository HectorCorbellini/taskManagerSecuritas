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
    
    /**
     * Process command-line arguments.
     * 
     * @param args command-line arguments
     * @throws SQLException if a database error occurs
     */
    public void processCommand(String[] args) throws SQLException {
        if (args.length < 1) {
            System.out.println("No command specified.");
            return;
        }
        
        String command = args[0];
        switch (command) {
            case "add-location":
                if (args.length < 4) {
                    System.out.println("Usage: add-location <name> <type> <address>");
                    return;
                }
                Location location = addLocation(args[1], args[2], args[3]);
                System.out.println("Location added with ID: " + location.getId());
                break;
                
            case "add-shift":
                if (args.length < 6) {
                    System.out.println("Usage: add-shift <locationId> <startTime> <endTime> <isArmed> <isRecurring> [recurrencePattern]");
                    return;
                }
                Long locationId = Long.parseLong(args[1]);
                LocalTime startTime = LocalTime.parse(args[2]);
                LocalTime endTime = LocalTime.parse(args[3]);
                boolean isArmed = Boolean.parseBoolean(args[4]);
                boolean isRecurring = Boolean.parseBoolean(args[5]);
                String recurrencePattern = args.length > 6 ? args[6] : "";
                
                Shift shift = addShift(locationId, startTime, endTime, isArmed, isRecurring, recurrencePattern);
                System.out.println("Shift added with ID: " + shift.getId());
                break;
                
            case "add-assignment":
                if (args.length < 4) {
                    System.out.println("Usage: add-assignment <shiftId> <date> <isReten> [notes]");
                    return;
                }
                Long shiftId = Long.parseLong(args[1]);
                LocalDate date = LocalDate.parse(args[2]);
                boolean isReten = Boolean.parseBoolean(args[3]);
                String notes = args.length > 4 ? args[4] : "";
                
                Assignment assignment = addAssignment(shiftId, date, isReten, notes);
                System.out.println("Assignment added with ID: " + assignment.getId());
                break;
                
            case "view-locations":
                List<Location> locations = getAllLocations();
                System.out.println("Locations:");
                for (Location loc : locations) {
                    System.out.println("ID: " + loc.getId() + ", Name: " + loc.getName() + ", Type: " + loc.getType() + ", Address: " + loc.getAddress());
                }
                break;
                
            case "view-shifts":
                List<Shift> shifts = getRecurringShifts();
                System.out.println("Shifts:");
                for (Shift s : shifts) {
                    System.out.println("ID: " + s.getId() + ", Location ID: " + s.getLocationId() + ", Start: " + s.getStartTime() + ", End: " + s.getEndTime() + ", Armed: " + s.isArmed() + ", Recurring: " + s.isRecurring());
                }
                break;
                
            case "view-assignments":
                if (args.length < 3) {
                    System.out.println("Usage: view-assignments <startDate> <endDate>");
                    return;
                }
                LocalDate startDate = LocalDate.parse(args[1]);
                LocalDate endDate = LocalDate.parse(args[2]);
                
                List<Assignment> assignments = getAssignmentsForDateRange(startDate, endDate);
                System.out.println("Assignments:");
                for (Assignment a : assignments) {
                    System.out.println("ID: " + a.getId() + ", Date: " + a.getAssignmentDate() + ", Shift ID: " + a.getShiftId() + ", Retén: " + a.isReten() + ", Status: " + a.getStatus());
                }
                break;
                
            case "is-rest-day":
                if (args.length < 2) {
                    System.out.println("Usage: is-rest-day <date>");
                    return;
                }
                LocalDate checkDate = LocalDate.parse(args[1]);
                boolean isRest = isRestDay(checkDate);
                System.out.println(checkDate + " is " + (isRest ? "a rest day." : "not a rest day."));
                break;
                
            default:
                System.out.println("Unknown command: " + command);
                break;
        }
    }
}
