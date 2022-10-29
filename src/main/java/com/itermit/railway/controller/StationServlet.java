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
 * Stations servlet controller.
 * <p>
 * Processes request and response by related command depend on URI.
 *
 * @author O.Savinov
 */
@WebServlet(name = "StationServlet",
        urlPatterns = {"/stations", "/stations/delete/*", "/stations/edit/*", "/stations/add"})
public class StationServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(StationServlet.class);


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

        if (request.getRequestURI().equals("/stations")) {

            CommandHandler.processForward("stationsList", request, response);

        } else if (request.getRequestURI().contains("/stations/delete")) {

            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/stations/edit")) {

            CommandHandler.processForward("stationEditGet", request, response);

        } else if (request.getRequestURI().contains("/stations/add")) {

            CommandHandler.processRedirect("stationAddGet", request, response);

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

        if (request.getRequestURI().contains("/stations/edit")) {

            CommandHandler.processRedirect("stationEditPost", request, response);

        } else if (request.getRequestURI().contains("/stations/delete")) {

            CommandHandler.processRedirect("stationDelete", request, response);

        } else if (request.getRequestURI().equals("/stations/add")) {

            CommandHandler.processRedirect("stationAddPost", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }
}
