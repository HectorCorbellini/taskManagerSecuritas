package com.securitas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import com.securitas.model.Assignment;
import com.securitas.model.Location;
import com.securitas.model.Shift;
import com.securitas.service.TaskManagerService;
import com.securitas.InputHandler;
import com.securitas.TaskService;

/**
 * Main application for managing assignments, locations, and shifts for security guards.
 */
public class TaskManagerApp {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerApp.class);
    private static final InputHandler inputHandler = new InputHandler();
    private static final TaskService taskService = new TaskService();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Main entry point of the application.
     * 
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        try {
            // Generate rest schedules for the next year
            LocalDate today = taskService.getDynamicDate();
            taskService.generateRestSchedules(today, 52);
            logger.info("Rest schedules generated for the next year.");
        } catch (SQLException e) {
            logger.error("Error generating rest schedules: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting dynamic date: " + e.getMessage());
        }

        if (args.length > 0) {
            try {
                taskService.processCommand(args);
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
                System.exit(1);
            }
            return;
        }
        while (true) {
            try {
                inputHandler.showMenu();
                String choice = inputHandler.getUserInput();

                switch (choice) {
                    case "1":
                        addLocation();
                        break;
                    case "2":
                        addShift();
                        break;
                    case "3":
                        addAssignment();
                        break;
                    case "4":
                        displayTomorrowAssignment();
                        break;
                    case "5":
                        viewAssignmentsByDateRange();
                        break;
                    case "6":
                        updateAssignment();
                        break;
                    case "7":
                        viewAllLocations();
                        break;
                    case "8":
                        viewRecurringShifts();
                        break;
                    case "9":
                        checkIfRestDay();
                        break;
                    case "0":
                        logger.info("Goodbye!");
                        return;
                    default:
                        logger.info("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                logger.info("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the main menu of the application.
     */
    private static void showMenu() {
        inputHandler.showMenu();
    }

    /**
     * Adds a new location based on user input.
     */
    private static void addLocation() {
        String name = inputHandler.getUserInput("Enter location name: ");
        String type = inputHandler.getUserInput("Enter location type: ");
        String address = inputHandler.getUserInput("Enter address: ");

        try {
            Location newLocation = taskService.addLocation(name, type, address);
            logger.info("Location added successfully with ID: " + newLocation.getId());
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding location: " + e.getMessage());
        }
    }

    /**
     * Adds a new shift based on user input.
     */
    private static void addShift() {
        Long locationId = Long.parseLong(inputHandler.getUserInput("Enter location ID: "));
        LocalTime startTime = LocalTime.parse(inputHandler.getUserInput("Enter start time (HH:mm): "), timeFormatter);
        LocalTime endTime = LocalTime.parse(inputHandler.getUserInput("Enter end time (HH:mm): "), timeFormatter);
        boolean isArmed = Boolean.parseBoolean(inputHandler.getUserInput("Is armed guard required? (true/false): "));
        boolean isRecurring = Boolean.parseBoolean(inputHandler.getUserInput("Is this a recurring shift? (true/false): "));
        String recurrencePattern = "";
        if (isRecurring) {
            recurrencePattern = inputHandler.getUserInput("Enter recurrence pattern (e.g., WEEKLY_MONDAY): ");
        }

        try {
            Shift newShift = taskService.addShift(locationId, startTime, endTime, isArmed, isRecurring, recurrencePattern);
            logger.info("Shift added successfully with ID: " + newShift.getId());
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding shift: " + e.getMessage());
        }
    }

    /**
     * Adds a new assignment based on user input.
     */
    private static void addAssignment() {
        Long shiftId = Long.parseLong(inputHandler.getUserInput("Enter shift ID: "));
        LocalDate assignmentDate = LocalDate.parse(inputHandler.getUserInput("Enter assignment date (yyyy-MM-dd): "), dateFormatter);
        boolean isReten = Boolean.parseBoolean(inputHandler.getUserInput("Is this a retén assignment? (true/false): "));
        String notes = inputHandler.getUserInput("Enter any notes: ");

        try {
            Assignment newAssignment = taskService.addAssignment(shiftId, assignmentDate, isReten, notes);
            logger.info("Assignment added successfully with ID: " + newAssignment.getId());
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error adding assignment: " + e.getMessage());
        }
    }

    /**
     * Displays the assignment scheduled for tomorrow.
     */
    private static void displayTomorrowAssignment() {
        LocalDate tomorrow;
        try {
            tomorrow = taskService.getDynamicDate().plusDays(1);
        } catch (Exception e) {
            logger.error("Error getting dynamic date: " + e.getMessage());
            return;
        }
        try {
            Assignment tomorrowAssignment = taskService.getAssignmentForDate(tomorrow);
            if (tomorrowAssignment != null) {
                printAssignmentDetails(tomorrowAssignment);
            } else {
                logger.info("No assignment found for tomorrow.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting assignment for date: " + e.getMessage());
        }
    }

    /**
     * Prints the details of a given assignment.
     * 
     * @param assignment the assignment to print details for.
     */
    private static void printAssignmentDetails(Assignment assignment) {
        logger.info("Tomorrow's Assignment:");
        logger.info("Date: " + assignment.getAssignmentDate());
        logger.info("Retén: " + (assignment.isReten() ? "Yes" : "No"));
        logger.info("Status: " + assignment.getStatus());
        logger.info("Notes: " + assignment.getNotes());
    }

    /**
     * Retrieves the current date dynamically from an API.
     * 
     * @return the current date as a LocalDate object.
     */
    private static LocalDate getDynamicDate() throws Exception {
        return taskService.getDynamicDate();
    }

    /**
     * Views assignments by a specified date range.
     */
    private static void viewAssignmentsByDateRange() {
        try {
            LocalDate startDate = LocalDate.parse(inputHandler.getUserInput("Enter start date (yyyy-MM-dd): "), dateFormatter);

            LocalDate endDate = LocalDate.parse(inputHandler.getUserInput("Enter end date (yyyy-MM-dd): "), dateFormatter);

            List<Assignment> assignments = taskService.getAssignmentsForDateRange(startDate, endDate);

            if (!assignments.isEmpty()) {
                logger.info("\nAssignments from " + startDate + " to " + endDate + ":");
                for (Assignment assignment : assignments) {
                    logger.info("\nDate: " + assignment.getAssignmentDate());
                    logger.info("Retén: " + (assignment.isReten() ? "Yes" : "No"));
                    logger.info("Status: " + assignment.getStatus());
                    logger.info("Notes: " + assignment.getNotes());
                }
            } else {
                logger.info("No assignments found for the specified date range.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting assignments for date range: " + e.getMessage());
        }
    }

    /**
     * Updates an assignment based on user input.
     */
    private static void updateAssignment() {
        try {
            Long assignmentId = Long.parseLong(inputHandler.getUserInput("Enter assignment ID: "));

            boolean isReten = Boolean.parseBoolean(inputHandler.getUserInput("Is this a retén assignment? (true/false): "));

            String status = inputHandler.getUserInput("Enter status (SCHEDULED/COMPLETED/CANCELLED): ");

            String notes = inputHandler.getUserInput("Enter notes: ");

            taskService.updateAssignmentDetails(assignmentId, isReten, status, notes);
            logger.info("Assignment updated successfully.");
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating assignment: " + e.getMessage());
        }
    }

    /**
     * Views all locations.
     */
    private static void viewAllLocations() {
        try {
            List<Location> locations = taskService.getAllLocations();

            if (!locations.isEmpty()) {
                logger.info("\nAll Locations:");
                for (Location location : locations) {
                    logger.info("\nID: " + location.getId());
                    logger.info("Name: " + location.getName());
                    logger.info("Type: " + location.getType());
                    logger.info("Address: " + location.getAddress());
                }
            } else {
                logger.info("No locations found.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting all locations: " + e.getMessage());
        }
    }

    /**
     * Views recurring shifts.
     */
    private static void viewRecurringShifts() {
        try {
            List<Shift> shifts = taskService.getRecurringShifts();

            if (!shifts.isEmpty()) {
                logger.info("\nRecurring Shifts:");
                for (Shift shift : shifts) {
                    logger.info("\nID: " + shift.getId());
                    logger.info("Location ID: " + shift.getLocationId());
                    logger.info("Start Time: " + shift.getStartTime());
                    logger.info("End Time: " + shift.getEndTime());
                    logger.info("Armed: " + (shift.isArmed() ? "Yes" : "No"));
                    logger.info("Recurrence Pattern: " + shift.getRecurrencePattern());
                }
            } else {
                logger.info("No recurring shifts found.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting recurring shifts: " + e.getMessage());
        }
    }

    /**
     * Checks if a specified date is a rest day.
     */
    private static void checkIfRestDay() {
        try {
            LocalDate date = LocalDate.parse(inputHandler.getUserInput("Enter date (yyyy-MM-dd): "), dateFormatter);

            boolean isRestDay = taskService.isRestDay(date);
            if (isRestDay) {
                logger.info(date + " is a rest day.");
            } else {
                logger.info(date + " is a work day.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error checking if date is rest day: " + e.getMessage());
        }
    }

    /**
     * Processes a command-line argument.
     * 
     * @param args command-line arguments.
     */
    private static void processCommand(String[] args) throws SQLException {
        taskService.processCommand(args);
    }
}
