package com.securitas.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to exit the application.
 */
public class ExitCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(ExitCommand.class);

    @Override
    public void execute() throws Exception {
        logger.info("Goodbye!");
        System.exit(0);
    }
}
