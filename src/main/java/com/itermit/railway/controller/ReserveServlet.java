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

@WebServlet(name = "ReserveServlet",
        urlPatterns = {"/reserves", "/reserves/delete/*", "/reserves/edit/*", "/reserves/add"})
public class ReserveServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ReserveServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().equals("/reserves")) {

            commandName = "reservesList";

        } else if (request.getRequestURI().contains("/reserves/delete")) {

            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/reserves/edit")) {

            commandName = "reserveEditGet";

        } else if (request.getRequestURI().contains("/reserves/add")) {

            commandName = "reserveAddGet";

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

        if (request.getRequestURI().contains("/reserves/edit")) {

            commandName = "reserveEditPost";

        } else if (request.getRequestURI().contains("/reserves/delete")) {

            commandName = "reserveDelete";

        } else if (request.getRequestURI().equals("/reserves/add")) {

            commandName = "reserveAddPost";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }
}
