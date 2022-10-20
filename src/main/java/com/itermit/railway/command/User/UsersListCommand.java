package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class UsersListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UsersListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        try {
            ArrayList<User> users = UserDAOImpl.getInstance().getAll();
            request.setAttribute("users", users);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        try {
            request.getRequestDispatcher("/users.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error users listing! {}", e.getMessage());
            throw new DBException("Error users listing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error users listing! {}", e.getMessage());
            throw new DBException("Error users listing!", e);
        }

        return null;
    }

}
