package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        UserManager.getInstance().delete(id);

        request.getSession().setAttribute("messages", "User deleted!");
        request.getSession().setAttribute("url", "/users");
        request.setAttribute("action", "delete");

        try {
            response.sendRedirect("/users");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting /users!", e);
        }

        return null;
    }

}
