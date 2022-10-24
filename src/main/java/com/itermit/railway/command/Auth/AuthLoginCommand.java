package com.itermit.railway.command.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AuthLoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthLoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

//        try {
//            request.setCharacterEncoding("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            logger.error("UnsupportedEncodingException. Error setCharacterEncoding! {}", e.getMessage());
//            throw new DBException("Error redirecting!", e);
//        }

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        User user = UserManager.getInstance().get(
                new User.Builder().withName(name).withPassword(password).build());

        if (user.getId() == 0) {
            logger.warn("401 Unauthorized");
            request.getSession().setAttribute("errors", "Login or password incorrect!");
        } else {
            logger.warn("User logged in: {}", user.getName());
            request.getSession().setAttribute("isAuthorized", true);
            request.getSession().setAttribute("isAdmin", user.getIsAdmin());
            request.getSession().setAttribute("userid", user.getId());
            request.getSession().setAttribute("username", user.getName());
            request.getSession().setAttribute("email", user.getEmail());
        }

        try {
            response.sendRedirect("/profile");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }


}
