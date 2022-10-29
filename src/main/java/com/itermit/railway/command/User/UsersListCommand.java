package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Command to get list of Users.
 * <p>
 * Processes GET-Request.
 * Displays list of Users.
 *
 * @author O.Savinov
 */
public class UsersListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UsersListCommand.class);

    /**
     * Command execution.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Address string
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        try {
            ArrayList<User> users = UserManager.getInstance().getAll();
            request.setAttribute("users", users);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/users.jsp";
    }

}
