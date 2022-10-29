package com.itermit.railway.controller;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Processes redirect and forward actions.
 * <p>
 * Handles related errors.
 *
 * @author O.Savinov
 */
public class CommandHandler {

    private static final Logger logger = LogManager.getLogger(CommandHandler.class);

    /**
     * Default constructor
     */
    private CommandHandler() {
    }

    /**
     * Processes redirect.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    public static String processRedirect(String commandName, HttpServletRequest request, HttpServletResponse response) {

        String address = null;

        try {
            address = CommandContainer.runCommand(request, response, commandName);
            if (address != null) {
                try {
                    response.sendRedirect(address);
                } catch (IOException e) {
                    logger.error("IOException. Error redirecting! {}", e.getMessage());
                    request.setAttribute("error", "Error while redirecting to " + address);
//                    throw new RuntimeException(e);
                }
            }
        } catch (CommandException e) {
            logger.error("CommandException. Error executing command! {}", e.getMessage());
            request.setAttribute("error", "Error while redirecting to " + address);
            try {
                request.getRequestDispatcher("/error").forward(request, response);
            } catch (ServletException ex) {
                logger.error("ServletException. Error forwarding! {}", e.getMessage());
                request.setAttribute("ex", ex);
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                logger.error("RuntimeException. Error forwarding! {}", e.getMessage());
                throw new RuntimeException(ex);
            }
        }

        return null;
    }

    /**
     * Processes forward.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void processForward(String commandName, HttpServletRequest request, HttpServletResponse response) {

        String address = null;

        try {
            address = CommandContainer.runCommand(request, response, commandName);
            if (address != null) {
                try {
                    request.getRequestDispatcher(address).forward(request, response);
                } catch (ServletException e) {
                    logger.error("ServletException. Error forwarding! {}", e.getMessage());
                    request.setAttribute("ex", e);
//                    throw new RuntimeException(e);
                } catch (IOException e) {
                    logger.error("RuntimeException. Error forwarding! {}", e.getMessage());
//                    throw new RuntimeException(e);
                }
            }
        } catch (CommandException e) {
            logger.error("CommandException. Error executing command! {}", e.getMessage());
            request.setAttribute("error", "Error while redirecting to " + address);
            try {
                request.getRequestDispatcher("/error").forward(request, response);
            } catch (ServletException ex) {
                logger.error("ServletException. Error forwarding! {}", e.getMessage());
                request.setAttribute("ex", ex);
//                throw new RuntimeException(ex);
            } catch (IOException ex) {
                logger.error("RuntimeException. Error forwarding! {}", e.getMessage());
//                throw new RuntimeException(ex);
            }
        }
    }

}
