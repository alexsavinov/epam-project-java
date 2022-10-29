package com.itermit.railway.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auth servlet controller.
 * <p>
 * Processes request and response by related command depending on URI.
 *
 * @author O.Savinov
 */
@WebServlet(name = "AuthServlet",
        urlPatterns = {"/login", "/logout", "/register", "/profile", "/activate/*"})
public class AuthServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthServlet.class);

    /**
     * Handles GET-Requests.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

    /**
     * Handles POST-Requests.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        /* Reset session attributes */
        CommandHandler.processRedirect("searchReset", request, response);

        if (request.getRequestURI().equals("/login")) {

            CommandHandler.processRedirect("authLogin", request, response);

        } else if (request.getRequestURI().equals("/logout")) {

            CommandHandler.processRedirect("authLogout", request, response);

        } else if (request.getRequestURI().equals("/register")) {

            CommandHandler.processRedirect("authRegister", request, response);

        } else if (request.getRequestURI().startsWith("/activate")) {

            CommandHandler.processRedirect("authActivate", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

}
