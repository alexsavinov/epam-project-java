package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to edit User.
 * <p>
 * Processes POST-Request.
 * Updates existed User in database.
 *
 * @author O.Savinov
 */
public class UserEditPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserEditPostCommand.class);

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

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = new User.Builder().withName(name).withPassword(password).build();
        try {
            UserManager.getInstance().update(id, user);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }
        request.getSession().setAttribute("messages", "User updated!");
        request.getSession().setAttribute("url", "/users");

        return "/users";
    }

}
