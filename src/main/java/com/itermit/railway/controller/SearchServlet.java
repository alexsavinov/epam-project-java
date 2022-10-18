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

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SearchServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName;

        if (request.getRequestURI().equals("/search")) {

            commandName = "searchGet";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("doGet UNHANDLED request!  {}", request.getRequestURI());
            return;
        }

        CommandContainer.runCommand(request, response, commandName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().equals("/search")) {

            String action = request.getParameter("action");
            logger.trace("doPost > action: {}", action);

            if (action != null && !action.isEmpty() && action.equals("reset")) {

                commandName = "searchReset";

            } else {

                commandName = "searchPost";

            }
        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

}