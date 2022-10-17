package com.itermit.railway.command;

import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserAddPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        User user = new User.Builder().withName(name).withPassword(password).build();
        UserDAOImpl.getInstance().add(user);

        request.getSession().setAttribute("messages", "User " + name + " added!");

        try {
            response.sendRedirect("/users");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}
