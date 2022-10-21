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

@WebServlet(name = "RouteServlet",
        urlPatterns = {
                "/routes",
                "/routes/delete/*",
                "/routes/edit/*",
                "/routes/add",
                "/routes/info/*"})
public class RouteServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RouteServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().equals("/routes")) {

            commandName = "routesList";

        } else if (request.getRequestURI().startsWith("/routes/edit")) {

            commandName = "routeEditGet";

        } else if (request.getRequestURI().equals("/routes/add")) {

            commandName = "routeAddGet";

        } else if (request.getRequestURI().startsWith("/routes/info")) {

            commandName = "routeInfo";

        } else if (request.getRequestURI().startsWith("/routes/delete")) {

            doPost(request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().startsWith("/routes/edit")) {

            commandName = "routeEditPost";

        } else if (request.getRequestURI().equals("/routes/add")) {

            commandName = "routeAddPost";

        } else if (request.getRequestURI().startsWith("/routes/delete")) {

            commandName = "routeDelete";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

}
