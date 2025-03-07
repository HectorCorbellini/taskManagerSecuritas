package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import com.securitas.model.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Command to add a new assignment.
 */
public class AddAssignmentCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddAssignmentCommand.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final InputHandler inputHandler;
    private final TaskService taskService;

    public AddAssignmentCommand(InputHandler inputHandler, TaskService taskService) {
        this.inputHandler = inputHandler;
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
        Long shiftId = Long.parseLong(inputHandler.getUserInput("Enter shift ID: "));
        LocalDate assignmentDate = LocalDate.parse(inputHandler.getUserInput("Enter assignment date (yyyy-MM-dd): "), dateFormatter);
        boolean isReten = Boolean.parseBoolean(inputHandler.getUserInput("Is this a retén assignment? (true/false): "));
        String notes = inputHandler.getUserInput("Enter notes: ");

        try {
            Assignment newAssignment = taskService.addAssignment(shiftId, assignmentDate, isReten, notes);
            logger.info("Assignment added successfully with ID: " + newAssignment.getId());
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error adding assignment: " + e.getMessage());
            throw e;
        }
    }
}
