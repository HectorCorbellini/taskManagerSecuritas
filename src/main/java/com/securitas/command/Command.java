package com.securitas.command;

/**
 * Command interface for executing actions based on user input.
 */
public interface Command {
    /**
     * Executes the command.
     * 
     * @throws Exception if an error occurs during execution
     */
    void execute() throws Exception;
}
