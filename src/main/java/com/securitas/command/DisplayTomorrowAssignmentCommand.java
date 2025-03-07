package com.securitas.command;

import com.securitas.TaskService;
import com.securitas.model.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Command to display tomorrow's assignment.
 */
public class DisplayTomorrowAssignmentCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(DisplayTomorrowAssignmentCommand.class);
    private final TaskService taskService;

    public DisplayTomorrowAssignmentCommand(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Assignment assignment = taskService.getAssignmentForDate(tomorrow);
            
            if (assignment != null) {
                printAssignmentDetails(assignment);
            } else {
                logger.info("No assignment scheduled for tomorrow.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error getting tomorrow's assignment: " + e.getMessage());
            throw e;
        }
    }
    
    private void printAssignmentDetails(Assignment assignment) {
        logger.info("\nAssignment Details:");
        logger.info("Date: " + assignment.getAssignmentDate());
        logger.info("Retén: " + (assignment.isReten() ? "Yes" : "No"));
        logger.info("Status: " + assignment.getStatus());
        logger.info("Notes: " + assignment.getNotes());
    }
}
