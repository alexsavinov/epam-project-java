package com.itermit.railway;


import com.itermit.railway.dao.entity.User;
//import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.itermit.railway.dao.UserDAO;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/logout", "/profile"})
public class AuthServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.trace("doGet {}", request.getRequestURI());

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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        logger.trace("doPost {}", request.getRequestURI());

        if (request.getRequestURI().equals("/login")) {

            doPostLogin(request, response);

        } else if (request.getRequestURI().equals("/logout")) {

            doPostLogout(request, response);

        }
    }

    protected void doPostLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        String hex = passwordEncrypt(request, response, password);

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
                request.getSession().setAttribute("isAuthorized", true);
                request.getSession().setAttribute("userid", user.getId());
                request.getSession().setAttribute("username", user.getName());
                request.getSession().setAttribute("isAdmin", user.getIsAdmin());
            }

            response.sendRedirect("/profile");

        } catch (DBException e) {

            logger.warn("Error authorizing user!  {}", e.getMessage());
            request.setAttribute("error", "Error authorizing user!");
            response.sendRedirect("/error");

            throw new RuntimeException(e);
        }
    }

    protected void doPostLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setAttribute("messages", "You are logged out!");
        request.getSession().setAttribute("isAuthorized", false);
        request.getSession().setAttribute("userid", null);
        request.getSession().setAttribute("username", null);
        request.getSession().setAttribute("isAdmin", null);

        response.sendRedirect("/");
    }

    protected static String passwordEncrypt(HttpServletRequest request,
                                            HttpServletResponse response,
                                            String password)
            throws IOException {

        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Error encrypting password!  {}", e.getMessage());
            request.setAttribute("error", "Error encrypting password!");
            response.sendRedirect("/error");
        }

        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        return String.format("%064x", new BigInteger(1, digest));
    }

}
