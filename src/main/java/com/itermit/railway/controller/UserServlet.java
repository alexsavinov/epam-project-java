package com.itermit.railway.controller;

import com.itermit.railway.command.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Handles user management.
 */
@MultipartConfig
@WebServlet(name = "UserServlet",
        urlPatterns = {"/users", "/users/delete/*", "/users/edit/*", "/users/add"})
public class UserServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName = null;
        if (request.getRequestURI().equals("/users")) {

            commandName = "usersList";

        } else if (request.getRequestURI().startsWith("/users/edit")) {

            commandName = "userEditGet";

        } else if (request.getRequestURI().equals("/users/add")) {

            commandName = "userAddGet";

        } else if (request.getRequestURI().startsWith("/users/delete")) {

            doPost(request, response);

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
        if (request.getRequestURI().startsWith("/users/edit")) {

            commandName = "userEditPost";

        } else if (request.getRequestURI().startsWith("/users/delete")) {

            commandName = "userDelete";

        } else if (request.getRequestURI().equals("/users/add")) {

            commandName = "userAddPost";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }
}
