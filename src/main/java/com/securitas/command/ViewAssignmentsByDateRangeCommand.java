package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import com.securitas.model.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Command to view assignments by date range.
 */
public class ViewAssignmentsByDateRangeCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ViewAssignmentsByDateRangeCommand.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final InputHandler inputHandler;
    private final TaskService taskService;

    public ViewAssignmentsByDateRangeCommand(InputHandler inputHandler, TaskService taskService) {
        this.inputHandler = inputHandler;
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
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
            throw e;
        } catch (Exception e) {
            logger.error("Error getting assignments by date range: " + e.getMessage());
            throw e;
        }
    }
}
