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
 * Search servlet controller.
 * <p>
 * Processes request and response by related command depend on URI.
 *
 * @author O.Savinov
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SearchServlet.class);

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

        if (request.getRequestURI().equals("/search")) {

            CommandHandler.processForward("searchGet", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("doGet UNHANDLED request!  {}", request.getRequestURI());
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

        if (request.getRequestURI().equals("/search")) {

            String action = request.getParameter("action");
//            logger.trace("doPost > action: {}", action);

            if (action != null && !action.isEmpty() && action.equals("reset")) {

                CommandHandler.processRedirect("searchReset", request, response);

            } else {

                CommandHandler.processRedirect("searchPost", request, response);

            }
        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

}