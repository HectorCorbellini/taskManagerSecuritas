package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Command to update an assignment.
 */
public class UpdateAssignmentCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(UpdateAssignmentCommand.class);
    private final InputHandler inputHandler;
    private final TaskService taskService;

    public UpdateAssignmentCommand(InputHandler inputHandler, TaskService taskService) {
        this.inputHandler = inputHandler;
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
        try {
            Long assignmentId = Long.parseLong(inputHandler.getUserInput("Enter assignment ID: "));
            boolean isReten = Boolean.parseBoolean(inputHandler.getUserInput("Is this a retén assignment? (true/false): "));
            String status = inputHandler.getUserInput("Enter status (SCHEDULED, COMPLETED, CANCELLED): ");
            String notes = inputHandler.getUserInput("Enter notes: ");
            
            taskService.updateAssignmentDetails(assignmentId, isReten, status, notes);
            logger.info("Assignment updated successfully.");
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating assignment: " + e.getMessage());
            throw e;
        }
    }
}
