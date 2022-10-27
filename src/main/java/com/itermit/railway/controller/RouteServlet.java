package com.itermit.railway.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RouteServlet",
        urlPatterns = {
                "/routes",
                "/routes/delete/*",
                "/routes/edit/*",
                "/routes/add",
                "/routes/info/*"})
public class RouteServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RouteServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        if (request.getRequestURI().equals("/routes")) {

            CommandHandler.processForward("routesList", request, response);

        } else if (request.getRequestURI().startsWith("/routes/edit")) {

            CommandHandler.processForward("routeEditGet", request, response);

        } else if (request.getRequestURI().equals("/routes/add")) {

            CommandHandler.processRedirect("routeAddGet", request, response);

        } else if (request.getRequestURI().startsWith("/routes/info")) {

            CommandHandler.processRedirect("routeInfo", request, response);

        } else if (request.getRequestURI().startsWith("/routes/delete")) {

            doPost(request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        if (request.getRequestURI().startsWith("/routes/edit")) {

            CommandHandler.processForward("routeEditPost", request, response);

        } else if (request.getRequestURI().equals("/routes/add")) {

            CommandHandler.processForward("routeAddPost", request, response);

        } else if (request.getRequestURI().startsWith("/routes/delete")) {

            CommandHandler.processForward("routeDelete", request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }
    }

}
