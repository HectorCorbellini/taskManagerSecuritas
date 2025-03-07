package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import com.securitas.model.Shift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Command to add a new shift.
 */
public class AddShiftCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddShiftCommand.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final InputHandler inputHandler;
    private final TaskService taskService;

    public AddShiftCommand(InputHandler inputHandler, TaskService taskService) {
        this.inputHandler = inputHandler;
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
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
            throw e;
        } catch (Exception e) {
            logger.error("Error adding shift: " + e.getMessage());
            throw e;
        }
    }
}
