package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to delete User.
 * <p>
 * Processes POST-Request.
 * Deletes User from database.
 *
 * @author O.Savinov
 */
public class UserDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserDeleteCommand.class);

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

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        try {
            UserManager.getInstance().delete(id);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        request.getSession().setAttribute("messages", "User deleted!");
        request.getSession().setAttribute("url", "/users");
        request.setAttribute("action", "delete");

        return "/users";
    }

}
