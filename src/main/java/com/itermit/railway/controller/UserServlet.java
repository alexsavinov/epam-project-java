package com.itermit.railway.controller;

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

        if (request.getRequestURI().equals("/users")) {

            CommandHandler.processForward("usersList", request, response);

        } else if (request.getRequestURI().startsWith("/users/edit")) {

            CommandHandler.processForward("userEditGet", request, response);

        } else if (request.getRequestURI().equals("/users/add")) {

            CommandHandler.processRedirect("userAddGet", request, response);

        } else if (request.getRequestURI().startsWith("/users/delete")) {

            doPost(request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        if (request.getRequestURI().startsWith("/users/edit")) {

            CommandHandler.processRedirect("userEditPost", request, response);

        } else if (request.getRequestURI().startsWith("/users/delete")) {

            CommandHandler.processRedirect("userDelete", request, response);

        } else if (request.getRequestURI().equals("/users/add")) {

            CommandHandler.processRedirect("userAddPost", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }
}
