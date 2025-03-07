package com.securitas;

import java.time.LocalDate;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.securitas.command.Command;
import com.securitas.command.CommandFactory;

/**
 * Main application for managing assignments, locations, and shifts for security guards.
 */
public class TaskManagerApp {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerApp.class);
    private static final InputHandler inputHandler = new InputHandler();
    private static final TaskService taskService = new TaskService();
    private static final CommandFactory commandFactory = new CommandFactory(inputHandler, taskService);

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
                processCommand(args);
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
                
                Command command = commandFactory.getCommand(choice);
                if (command != null) {
                    command.execute();
                }
            } catch (Exception e) {
                logger.info("An error occurred: " + e.getMessage());
            }
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
