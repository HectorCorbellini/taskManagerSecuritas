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

/**
 * Main application for managing assignments, locations, and shifts for security guards.
 */
public class TaskManagerApp {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerApp.class);
    private static final TaskManagerService service = new TaskManagerService();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main entry point of the application.
     * 
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        try {
            // Generate rest schedules for the next year
            LocalDate today = getDynamicDate();
            service.generateRestSchedules(today, 52);
            logger.info("Rest schedules generated for the next year.");
        } catch (SQLException e) {
            logger.error("Error generating rest schedules: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error getting dynamic date: " + e.getMessage());
        }

        if (args.length > 0) {
            try {
                processCommand(args);
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
                System.exit(1);
            }
            return;
        }
        while (true) {
            try {
                showMenu();
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> addLocation();
                    case 2 -> addShift();
                    case 3 -> addAssignment();
                    case 4 -> displayTomorrowAssignment();
                    case 5 -> viewAssignmentsByDateRange();
                    case 6 -> updateAssignment();
                    case 7 -> viewAllLocations();
                    case 8 -> viewRecurringShifts();
                    case 9 -> checkIfRestDay();
                    case 0 -> {
                        logger.info("Goodbye!");
                        return;
                    }
                    default -> logger.info("Invalid choice. Please try again.");
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
        logger.info("\n===== Task Manager Menu =====");
        logger.info("1. Add New Location");
        logger.info("2. Add New Shift");
        logger.info("3. Add New Assignment");
        logger.info("4. View Tomorrow's Assignment");
        logger.info("5. View Assignments by Date Range");
        logger.info("6. Update Assignment");
        logger.info("7. View All Locations");
        logger.info("8. View Recurring Shifts");
        logger.info("9. Check if Date is Rest Day");
        logger.info("0. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Adds a new location based on user input.
     */
    private static void addLocation() {
        System.out.print("Enter location name: ");
        String name = scanner.nextLine();
        System.out.print("Enter location type: ");
        String type = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        try {
            Location newLocation = service.addLocation(name, type, address);
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
        System.out.print("Enter location ID: ");
        Long locationId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter start time (HH:mm): ");
        LocalTime startTime = LocalTime.parse(scanner.nextLine(), timeFormatter);
        System.out.print("Enter end time (HH:mm): ");
        LocalTime endTime = LocalTime.parse(scanner.nextLine(), timeFormatter);
        System.out.print("Is armed guard required? (true/false): ");
        boolean isArmed = Boolean.parseBoolean(scanner.nextLine());
        System.out.print("Is this a recurring shift? (true/false): ");
        boolean isRecurring = Boolean.parseBoolean(scanner.nextLine());
        String recurrencePattern = "";
        if (isRecurring) {
            System.out.print("Enter recurrence pattern (e.g., WEEKLY_MONDAY): ");
            recurrencePattern = scanner.nextLine();
        }

        try {
            Shift newShift = service.addShift(locationId, startTime, endTime, isArmed, isRecurring, recurrencePattern);
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
        System.out.print("Enter shift ID: ");
        Long shiftId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter assignment date (yyyy-MM-dd): ");
        LocalDate assignmentDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
        System.out.print("Is this a retén assignment? (true/false): ");
        boolean isReten = Boolean.parseBoolean(scanner.nextLine());
        System.out.print("Enter any notes: ");
        String notes = scanner.nextLine();

        try {
            Assignment newAssignment = service.addAssignment(shiftId, assignmentDate, isReten, notes);
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
            tomorrow = getDynamicDate().plusDays(1);
        } catch (Exception e) {
            logger.error("Error getting dynamic date: " + e.getMessage());
            return;
        }
        try {
            Assignment tomorrowAssignment = service.getAssignmentForDate(tomorrow);
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
        URL url = new URL("http://worldtimeapi.org/api/timezone/Etc/UTC");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String jsonResponse = response.toString();
        String currentDate = jsonResponse.split("\\\"")[1].split(",")[0].split(":")[1].replaceAll("\\\"", "").trim();
        return LocalDate.parse(currentDate);
    }

    /**
     * Views assignments by a specified date range.
     */
    private static void viewAssignmentsByDateRange() {
        try {
            System.out.print("Enter start date (yyyy-MM-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            System.out.print("Enter end date (yyyy-MM-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);

            List<Assignment> assignments = service.getAssignmentsForDateRange(startDate, endDate);

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
            System.out.print("Enter assignment ID: ");
            Long assignmentId = Long.parseLong(scanner.nextLine());

            System.out.print("Is this a retén assignment? (true/false): ");
            boolean isReten = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("Enter status (SCHEDULED/COMPLETED/CANCELLED): ");
            String status = scanner.nextLine();

            System.out.print("Enter notes: ");
            String notes = scanner.nextLine();

            service.updateAssignmentDetails(assignmentId, isReten, status, notes);
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
            List<Location> locations = service.getAllLocations();

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
            List<Shift> shifts = service.getRecurringShifts();

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
            System.out.print("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine(), dateFormatter);

            boolean isRestDay = service.isRestDay(date);
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
        String command = args[0];

        switch (command) {
            case "add-location" -> {
                if (args.length != 4) {
                    throw new IllegalArgumentException("Usage: add-location <name> <type> <address>");
                }
                Location location = service.addLocation(args[1], args[2], args[3]);
                logger.info("Location added successfully with ID: " + location.getId());
            }
            case "add-shift" -> {
                if (args.length != 7) {
                    throw new IllegalArgumentException("Usage: add-shift <locationId> <startTime> <endTime> <isArmed> <isRecurring> <recurrencePattern>");
                }
                Long locationId = Long.parseLong(args[1]);
                LocalTime startTime = LocalTime.parse(args[2], timeFormatter);
                LocalTime endTime = LocalTime.parse(args[3], timeFormatter);
                boolean isArmed = Boolean.parseBoolean(args[4]);
                boolean isRecurring = Boolean.parseBoolean(args[5]);
                Shift shift = service.addShift(locationId, startTime, endTime, isArmed, isRecurring, args[6]);
                logger.info("Shift added successfully with ID: " + shift.getId());
            }
            case "add-assignment" -> {
                if (args.length != 5) {
                    throw new IllegalArgumentException("Usage: add-assignment <shiftId> <date> <isReten> <notes>");
                }
                Long shiftId = Long.parseLong(args[1]);
                LocalDate date = LocalDate.parse(args[2], dateFormatter);
                boolean isReten = Boolean.parseBoolean(args[3]);
                try {
                    Assignment assignment = service.addAssignment(shiftId, date, isReten, args[4]);
                    logger.info("Assignment added successfully with ID: " + assignment.getId());
                } catch (SQLException e) {
                    logger.error("SQL Error: " + e.getMessage());
                } catch (Exception e) {
                    logger.error("Error adding assignment: " + e.getMessage());
                }
            }
            case "view-tomorrow" -> {
                try {
                    Assignment assignment = service.getAssignmentForDate(getDynamicDate().plusDays(1));
                    if (assignment != null) {
                        printAssignmentDetails(assignment);
                    } else {
                        logger.info("No assignment found for tomorrow.");
                    }
                } catch (SQLException e) {
                    logger.error("SQL Error: " + e.getMessage());
                } catch (Exception e) {
                    logger.error("Error getting assignment for date: " + e.getMessage());
                }
            }
            case "view-locations" -> {
                List<Location> locations = service.getAllLocations();
                if (!locations.isEmpty()) {
                    logger.info("All Locations:");
                    for (Location location : locations) {
                        logger.info("\nID: " + location.getId());
                        logger.info("Name: " + location.getName());
                        logger.info("Type: " + location.getType());
                        logger.info("Address: " + location.getAddress());
                    }
                } else {
                    logger.info("No locations found.");
                }
            }
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        }
    }
}
