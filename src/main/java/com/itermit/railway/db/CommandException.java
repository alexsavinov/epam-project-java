package com.itermit.railway.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandException extends Exception {

    private static final Logger logger = LogManager.getLogger(CommandException.class);

    public CommandException(String message, Throwable cause) {
        super(message, cause);
        logger.error("{}", message);
    }

}
