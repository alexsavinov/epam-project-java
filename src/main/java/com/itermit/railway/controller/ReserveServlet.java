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
 * Reserves servlet controller.
 * <p>
 * Processes request and response by related command depend on URI.
 *
 * @author O.Savinov
 */
@WebServlet(name = "ReserveServlet",
        urlPatterns = {"/reserves/grouped", "/reserves", "/reserves/delete", "/reserves/edit/*", "/reserves/add"})
public class ReserveServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ReserveServlet.class);

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

        if (request.getRequestURI().equals("/reserves")) {

            CommandHandler.processForward("reservesList", request, response);

        } else if (request.getRequestURI().equals("/reserves/grouped")) {

            CommandHandler.processRedirect("reservesGroupedList", request, response);

        } else if (request.getRequestURI().contains("/reserves/delete")) {

            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/reserves/edit")) {

            CommandHandler.processForward("reserveEditGet", request, response);

        } else if (request.getRequestURI().contains("/reserves/add")) {

            CommandHandler.processRedirect("reserveAddGet", request, response);

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        if (request.getRequestURI().contains("/reserves/edit")) {

            CommandHandler.processRedirect("reserveEditPost", request, response);

        } else if (request.getRequestURI().contains("/reserves/delete")) {

            CommandHandler.processRedirect("reserveDelete", request, response);

        } else if (request.getRequestURI().equals("/reserves/add")) {

            CommandHandler.processRedirect("reserveAddPost", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }
}
