package com.itermit.railway.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ControllerException extends Exception {

    private static final Logger logger = LogManager.getLogger(ControllerException.class);

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
        logger.error(" -> ControllerException: {}", message);
    }

}
