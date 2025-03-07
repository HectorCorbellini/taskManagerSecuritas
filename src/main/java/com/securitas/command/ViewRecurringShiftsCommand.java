package com.securitas.command;

import com.securitas.TaskService;
import com.securitas.model.Shift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Command to view recurring shifts.
 */
public class ViewRecurringShiftsCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ViewRecurringShiftsCommand.class);
    private final TaskService taskService;

    public ViewRecurringShiftsCommand(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
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
            throw e;
        } catch (Exception e) {
            logger.error("Error getting recurring shifts: " + e.getMessage());
            throw e;
        }
    }
}
