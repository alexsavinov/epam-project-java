package com.itermit.railway;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;

import static javax.servlet.RequestDispatcher.*;


//@WebServlet(name = "InitServlet", value = "/InitServlet")
@WebServlet(name = "InitServlet", urlPatterns = {"/error"})
public class InitServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(InitServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.trace("#doGet(request, response)");

        if (request.getRequestURI().equals("/error")) {
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.write("<h2>Error description</h2>");
            out.write("<ul>");
            Arrays.asList(ERROR_STATUS_CODE, ERROR_EXCEPTION_TYPE, ERROR_MESSAGE)
                    .forEach(e -> out.write("<li>" + e + ":" + request.getAttribute(e) + " </li>"));
            out.write("</ul>");

            String error = (String) Objects.requireNonNull(request.getAttribute("error"));
            if (!error.isEmpty()) {
                logger.error(error);
                out.write("<h2>" + error + "</h2>");
//                request.getSession().removeAttribute("error");
            }
            out.println("</body></html>");
        } else {
            logger.info("request.getQueryString(): {}", request.getQueryString());
            logger.info("request.getPathInfo(): {}", request.getPathInfo());
            logger.info("request.getRequestURI(): {}", request.getRequestURI());
            logger.error("Unhandled request!  {}", request.getRequestURI());
        }
    }

}