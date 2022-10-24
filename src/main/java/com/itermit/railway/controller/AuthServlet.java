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

/*
 * Handles authorization process.
 */
@WebServlet(name = "AuthServlet",
        urlPatterns = {"/login", "/logout", "/register", "/profile", "/activate/*"})
public class AuthServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        if (request.getRequestURI().equals("/profile")) {
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } else if (request.getRequestURI().equals("/login")) {
            request.getRequestDispatcher("/auth.jsp").forward(request, response);
        } else if (request.getRequestURI().equals("/register")) {
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else if (request.getRequestURI().equals("/logout")) {
            doPost(request, response);
        } else if (request.getRequestURI().startsWith("/activate")) {
            doPost(request, response);
        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        /* Reset session attributes */
        String commandName = "searchReset";
        CommandContainer.runCommand(request, response, commandName);

        if (request.getRequestURI().equals("/login")) {

            commandName = "authLogin";

        } else if (request.getRequestURI().equals("/logout")) {

            commandName = "authLogout";


        } else if (request.getRequestURI().equals("/register")) {

            commandName = "authRegister";


        } else if (request.getRequestURI().startsWith("/activate")) {

            commandName = "authActivate";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

}
