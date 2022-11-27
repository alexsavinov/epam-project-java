package com.itermit.railway.command.Auth;

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
 * Command to activate User.
 *
 * @author O.Savinov
 */
public class AuthActivateCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthActivateCommand.class);

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

        String activationToken = CommandContainer.getTokenFromRequest(request);

        User user;
        try {
            user = UserManager.getInstance().activate(activationToken);
        } catch (DBException e) {
            request.getSession().setAttribute("errors", e.getMessage());
            return "/login";
        }

        logger.info("activationToken {}", activationToken);
        request.getSession().setAttribute("messages", "User activated: " + user.getName());

        return "/login";
    }

}
