package com.itermit.railway.controller;


import com.itermit.railway.dao.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.itermit.railway.dao.UserDAO;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Handles authorization process.
 */

@WebServlet(name = "AuthServlet",
        urlPatterns = {"/login", "/logout", "/profile"})
public class AuthServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        logger.trace("#doGet(request, response).  {}", request.getRequestURI());

        try {
            if (request.getRequestURI().equals("/profile")) {

                request.getRequestDispatcher("/profile.jsp").forward(request, response);

            } else if (request.getRequestURI().equals("/login")) {

                request.getRequestDispatcher("/auth.jsp").forward(request, response);

            } else if (request.getRequestURI().equals("/logout")) {

                doPost(request, response);

            } else {

                request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
                request.getRequestDispatcher("/error").forward(request, response);

                logger.error("doGet UNHANDLED request!  {}", request.getRequestURI());
            }
        } catch (ServletException e) {
            logger.error("ServletException. Error authorizing user!  {}", e.getMessage());
        } catch (IOException e) {
            logger.error("IOException. Error authorizing user!  {}", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        logger.trace("#doPost(request, response).  {}", request.getRequestURI());


        try {
            if (request.getRequestURI().equals("/login")) {
                doPostLogin(request, response);
            } else if (request.getRequestURI().equals("/logout")) {
                doPostLogout(request, response);
            }
        } catch (ControllerException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.getRequestDispatcher("/error").forward(request, response);
            } catch (ServletException ex) {
                logger.error("ServletException. Error authorizing user!  {}", ex.getMessage());
            } catch (IOException ex) {
                logger.error("IOException. Error authorizing user!  {}", ex.getMessage());
            }
        }
    }

    protected void doPostLogin(HttpServletRequest request, HttpServletResponse response)
            throws ControllerException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        String hex = passwordEncrypt(password);

        logger.warn("password (SHA-256): {}", hex);

        User user = new User.Builder().withName(name).withPassword(hex).build();
        UserDAO userDAO = new UserDAOImpl();

        try {
            user = userDAO.get(user);
            logger.info(user);

            if (user.getId() == 0) {
                logger.warn("401 Unauthorized");
                request.setAttribute("errors", "401 Unauthorized");
                response.sendRedirect("/");
            } else {
                logger.warn("User logged in: {}", user.getName());

                request.getSession().setAttribute("isAuthorized", true);
                request.getSession().setAttribute("isAdmin", user.getIsAdmin());
                request.getSession().setAttribute("userid", user.getId());
                request.getSession().setAttribute("username", user.getName());
            }
            response.sendRedirect("/profile");

        } catch (DBException e) {

            logger.error("DBException. Error authorizing user!  {}", e.getMessage());
            throw new ControllerException("Error authorizing user!", e);

        } catch (IOException e) {

            logger.error("IOException. Error during redirect!  {}", e.getMessage());
            throw new ControllerException("Error redirect to page!", e);
        }
    }

    protected void doPostLogout(HttpServletRequest request, HttpServletResponse response)
            throws ControllerException {

        String username = (String) request.getSession().getAttribute("username");
        request.getSession().removeAttribute("isAuthorized");
        request.getSession().removeAttribute("userid");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("isAdmin");
        request.getSession().removeAttribute("orders");
        request.getSession().removeAttribute("routes");


        try {
            logger.warn("User logged out: {}", username);
            response.sendRedirect("/");
        } catch (IOException e) {
            logger.error("IOException. Error during redirect!  {}", e.getMessage());
            throw new ControllerException("Error redirect to page!", e);
        }
    }

    protected static String passwordEncrypt(String password) throws ControllerException {

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException. Error encrypting password!  {}", e.getMessage());
            throw new ControllerException("Error encrypting password!", e);
        }

        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        return String.format("%064x", new BigInteger(1, digest));
    }

}
