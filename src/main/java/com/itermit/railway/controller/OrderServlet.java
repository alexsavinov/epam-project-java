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

@WebServlet(name = "OrderServlet",
        urlPatterns = {"/orders", "/orders/delete/*", "/orders/edit/*", "/orders/add"})
public class OrderServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(OrderServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().equals("/orders")) {

            commandName = "ordersList";

        } else if (request.getRequestURI().startsWith("/orders/delete")) {

            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/orders/edit")) {

            commandName = "orderEditGet";

        } else if (request.getRequestURI().startsWith("/orders/add")) {

            commandName = "orderAddGet";

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

        if (request.getRequestURI().startsWith("/orders/edit")) {

            commandName = "orderEditPost";

        } else if (request.getRequestURI().startsWith("/orders/delete")) {

            commandName = "orderDelete";

        } else if (request.getRequestURI().equals("/orders/add")) {

            commandName = "orderAddPost";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

}