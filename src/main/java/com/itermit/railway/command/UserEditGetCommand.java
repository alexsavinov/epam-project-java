package com.itermit.railway.command;

import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        User user = UserDAOImpl.getInstance().get(id);
        request.setAttribute("user", user);
        request.setAttribute("action", "edit");

        try {
            request.getRequestDispatcher("/user.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error users listing! {}", e.getMessage());
            throw new DBException("Error user editing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error users listing! {}", e.getMessage());
            throw new DBException("Error user editing!", e);
        }

        return null;
    }

}
