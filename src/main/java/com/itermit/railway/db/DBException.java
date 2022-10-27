package com.itermit.railway.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBException extends Exception {

    private static final Logger logger = LogManager.getLogger(DBException.class);

    public DBException(String message, Throwable cause) {
        super(message, cause);
        logger.error("{}", message);
    }

}
