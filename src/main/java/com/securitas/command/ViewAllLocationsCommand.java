package com.securitas.command;

import com.securitas.TaskService;
import com.securitas.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Command to view all locations.
 */
public class ViewAllLocationsCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ViewAllLocationsCommand.class);
    private final TaskService taskService;

    public ViewAllLocationsCommand(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
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
            throw e;
        } catch (Exception e) {
            logger.error("Error getting all locations: " + e.getMessage());
            throw e;
        }
    }
}
