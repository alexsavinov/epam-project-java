package com.itermit.railway.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides information about commands execution errors.
 *
 * @author O.Savinov
 */
public class CommandException extends Exception {

    private static final Logger logger = LogManager.getLogger(CommandException.class);

    /**
     * Default constructor
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
        logger.error("{}", message);
    }

}
