package com.itermit.railway.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides information about Database access errors.
 *
 * @author O.Savinov
 */
public class DBException extends Exception {

    private static final Logger logger = LogManager.getLogger(DBException.class);

    /**
     * Default constructor
     */
    public DBException(String message, Throwable cause) {
        super(message, cause);
        logger.error("{}", message);
    }

}
