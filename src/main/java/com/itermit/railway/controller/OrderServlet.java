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
 * Orders servlet controller.
 * <p>
 * Processes request and response by related command depend on URI.
 *
 * @author O.Savinov
 */
@WebServlet(name = "OrderServlet",
        urlPatterns = {"/orders", "/orders/delete/*", "/orders/edit/*", "/orders/add"})
public class OrderServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(OrderServlet.class);

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

        if (request.getRequestURI().equals("/orders")) {

            CommandHandler.processForward("ordersList", request, response);

        } else if (request.getRequestURI().startsWith("/orders/delete")) {

            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/orders/edit")) {

            CommandHandler.processForward("orderEditGet", request, response);

        } else if (request.getRequestURI().startsWith("/orders/add")) {

            CommandHandler.processForward("orderAddGet", request, response);

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

        if (request.getRequestURI().startsWith("/orders/edit")) {

            CommandHandler.processRedirect("orderEditPost", request, response);

        } else if (request.getRequestURI().startsWith("/orders/delete")) {

            CommandHandler.processRedirect("orderDelete", request, response);

        } else if (request.getRequestURI().equals("/orders/add")) {

            CommandHandler.processRedirect("orderAddPost", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

}