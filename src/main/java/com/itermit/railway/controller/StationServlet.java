package com.itermit.railway.controller;

import com.itermit.railway.command.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StationServlet",
        urlPatterns = {"/stations", "/stations/delete/*", "/stations/edit/*", "/stations/add"})
public class StationServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(StationServlet.class);

    /*
     * Processes GET-request
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().equals("/stations")) {

            commandName = "stationsList";

        } else if (request.getRequestURI().contains("/stations/delete")) {

            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/stations/edit")) {

            commandName = "stationEditGet";

        } else if (request.getRequestURI().contains("/stations/add")) {

            commandName = "stationAddGet";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().contains("/stations/edit")) {

            commandName = "stationEditPost";

        } else if (request.getRequestURI().contains("/stations/delete")) {

            commandName = "stationDelete";

        } else if (request.getRequestURI().equals("/stations/add")) {

            commandName = "stationAddPost";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }
}
