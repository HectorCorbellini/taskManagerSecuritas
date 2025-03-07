package com.securitas.command;

import com.securitas.InputHandler;
import com.securitas.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating Command objects based on user input.
 */
public class CommandFactory {
    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);
    private final Map<String, Command> commands = new HashMap<>();
    
    /**
     * Creates a new CommandFactory with the specified InputHandler and TaskService.
     * 
     * @param inputHandler the input handler
     * @param taskService the task service
     */
    public CommandFactory(InputHandler inputHandler, TaskService taskService) {
        commands.put("1", new AddLocationCommand(inputHandler, taskService));
        commands.put("2", new AddShiftCommand(inputHandler, taskService));
        commands.put("3", new AddAssignmentCommand(inputHandler, taskService));
        commands.put("4", new DisplayTomorrowAssignmentCommand(taskService));
        commands.put("5", new ViewAssignmentsByDateRangeCommand(inputHandler, taskService));
        commands.put("6", new UpdateAssignmentCommand(inputHandler, taskService));
        commands.put("7", new ViewAllLocationsCommand(taskService));
        commands.put("8", new ViewRecurringShiftsCommand(taskService));
        commands.put("9", new CheckIfRestDayCommand(inputHandler, taskService));
        commands.put("0", new ExitCommand());
    }
    
    /**
     * Gets the command for the specified choice.
     * 
     * @param choice the user's choice
     * @return the command for the choice, or null if no command exists for the choice
     */
    public Command getCommand(String choice) {
        Command command = commands.get(choice);
        if (command == null) {
            logger.info("Invalid choice. Please try again.");
        }
        return command;
    }
}
