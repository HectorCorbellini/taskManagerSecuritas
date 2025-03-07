package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import com.securitas.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Command to add a new location.
 */
public class AddLocationCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(AddLocationCommand.class);
    private final InputHandler inputHandler;
    private final TaskService taskService;

    public AddLocationCommand(InputHandler inputHandler, TaskService taskService) {
        this.inputHandler = inputHandler;
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
        String name = inputHandler.getUserInput("Enter location name: ");
        String type = inputHandler.getUserInput("Enter location type: ");
        String address = inputHandler.getUserInput("Enter address: ");

        try {
            Location newLocation = taskService.addLocation(name, type, address);
            logger.info("Location added successfully with ID: " + newLocation.getId());
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error adding location: " + e.getMessage());
            throw e;
        }
    }
}
