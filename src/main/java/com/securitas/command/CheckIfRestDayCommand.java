package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Command to check if a date is a rest day.
 */
public class CheckIfRestDayCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CheckIfRestDayCommand.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final InputHandler inputHandler;
    private final TaskService taskService;

    public CheckIfRestDayCommand(InputHandler inputHandler, TaskService taskService) {
        this.inputHandler = inputHandler;
        this.taskService = taskService;
    }

    @Override
    public void execute() throws Exception {
        try {
            LocalDate date = LocalDate.parse(inputHandler.getUserInput("Enter date (yyyy-MM-dd): "), dateFormatter);

            boolean isRestDay = taskService.isRestDay(date);
            if (isRestDay) {
                logger.info(date + " is a rest day.");
            } else {
                logger.info(date + " is a work day.");
            }
        } catch (SQLException e) {
            logger.error("SQL Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error checking if date is rest day: " + e.getMessage());
            throw e;
        }
    }
}
