package com.itermit.railway;


import com.itermit.railway.dao.entity.User;
//import org.apache.commons.codec.digest.DigestUtils;
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

@WebServlet(name = "AuthServlet", value = "/AuthServlet")
public class AuthServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String name = request.getParameter("name");
//        String password = request.getParameter("password");
//        String actionName = (String) request.getSession().getAttribute("actionName");

//        request.getSession().setAttribute("messages", "");

        logger.info("doGet getRequestURI -- " + request.getRequestURI());

//        Boolean isAuthorized = false;
//        if (request.getSession().getAttribute("isAuthorized") != null) {
//            isAuthorized = (Boolean) request.getSession().getAttribute("isAuthorized");
//        }
//        doPost(request, response);
//
//        return;

//        logger.info("doGet actionName = " + actionName);
//        logger.info("doGet isAuthorized = " + isAuthorized);
//        if (isAuthorized) {
////            logger.info("!!!!!!!isAuthorized doGet");

        if (request.getRequestURI().equals("/profile")) {
            logger.info("doGet profile -- " + request.getRequestURI());

            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } else if (request.getRequestURI().equals("/auth")) {
            logger.info("doGet auth -- " + request.getRequestURI());

            request.getRequestDispatcher("/auth.jsp").forward(request, response);
//            doPost(request, response);
        } else {
            logger.info("doGet UNHANDLED request.getRequestURI() -- " + request.getRequestURI());
//            response.sendRedirect("/");

        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(request.getParameter("name"));

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String actionName = (String) request.getSession().getAttribute("actionName");
        Boolean isAuthorized = false;
        if (request.getSession().getAttribute("isAuthorized") != null) {
            isAuthorized = (Boolean) request.getSession().getAttribute("isAuthorized");
        }
//        Boolean isAuthorized = request.getSession().getAttribute("isAuthorized");

        logger.info("doPost actionName = ." + actionName + ".");


//        if (name != null && password != null) {
//        if (actionName == null || actionName.isEmpty()) {
        if (isAuthorized.equals(false)) {
//            request.setAttribute("name", name);
//            request.setAttribute("password", password);


            // TODO: 256
//            User user = new User.Builder().withName(name).withPassword(DigestUtils.sha256Hex(password)).build();
            User user = new User.Builder().withName(name).withPassword(password).build();
//                    0, name, DigestUtils.sha256Hex(password), false);

            UserDAO userDAO = new UserDAOImpl();
            try {
                user = userDAO.get(user);
                logger.info(user);

                try {
                    if (user.getId() == 0) {
                        request.setAttribute("errors", "401 Unauthorized");
                        response.sendRedirect("/");
                        return;

                    } else {
                        logger.info("doPost " + user);
//                        request.getSession().setAttribute("messages", "You are authorized!");
                        request.setAttribute("messages", "You are authorized!");
                        request.getSession().setAttribute("actionName", "login");
                        request.getSession().setAttribute("isAuthorized", true);
                        request.getSession().setAttribute("username", user.getName());
                        request.getSession().setAttribute("userid", user.getId());
                        request.getSession().setAttribute("isAdmin", user.getIsAdmin());
//                        request.getRequestDispatcher("auth.jsp").forward(request, response);
                    }
                    logger.info("doPost auth.jsp--forward userId:" + user.getId());
                    request.getRequestDispatcher("/auth.jsp").forward(request, response);
                } catch (ServletException e) {
                    throw new RuntimeException(e);
                }
            } catch (DBException e) {
                throw new RuntimeException(e);
            }

//            request.setAttribute("users", users);
//
//            try {
//                request.getRequestDispatcher("users.jsp").forward(request, response);
//            } catch (ServletException e) {
//                throw new RuntimeException(e);
//            }


//        } else if (actionName.equals("loginSuccessful")) {
//        } else if (actionName.equals("loginSuccessful")) {
//            logger.info("doPost loginSuccessful:" + "");
//
//            request.setAttribute("messages", "MESSAGES");
//            request.getRequestDispatcher("message.jspf").forward(request, response);


//            try {
////                if (user.getId() == 0) {
////                    request.setAttribute("errors", "401 Unauthorized");
////                } else {
////                    logger.info("doPost user.getId() " + user.getId() + " name:" + user.getName()
////                            + " pass:" + user.getPassword() + " isadmin:" + user.getIsAdmin());
////                    request.setAttribute("messages", "200 OK");
//////                        request.getRequestDispatcher("auth.jsp").forward(request, response);
////                }
////                logger.info("doPost auth.jsp--forward userId:" + user.getId());
//
////                request.getSession().setAttribute("actionName", actionName);
//                request.getRequestDispatcher("auth.jsp").forward(request, response);
//            } catch (ServletException e) {
//                throw new RuntimeException(e);
//            }


        } else {

            try {
//                logger.info("doPost user.getId() " + user.getId() + " name:" + user.getName()
//                        + " pass:" + user.getPassword() + " isadmin:" + user.getIsAdmin());
//                request.getSession().setAttribute("messages", "You are logged out!");
                request.setAttribute("messages", "You are logged out!");
                request.getSession().setAttribute("actionName", "logout");
                request.getSession().setAttribute("isAuthorized", false);
                request.getSession().setAttribute("user", null);

                logger.info("doPost auth.jsp--forward LOGOUT");
                request.getRequestDispatcher("/auth.jsp").forward(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
