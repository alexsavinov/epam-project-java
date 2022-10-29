package com.itermit.railway.command.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command to authenticate User.
 *
 * @author O.Savinov
 */
public class AuthLoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthLoginCommand.class);

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

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        User user;
        try {
            user = UserManager.getInstance().get(
                    new User.Builder().withName(name).withPassword(password).build());

            logger.warn("User logged in: {}", user.getName());
            request.getSession().setAttribute("isAuthorized", true);
            request.getSession().setAttribute("isAdmin", user.getIsAdmin());
            request.getSession().setAttribute("userid", user.getId());
            request.getSession().setAttribute("username", user.getName());
            request.getSession().setAttribute("email", user.getEmail());
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        if (user.getId() == 0) {
            logger.error("CommandException. 401 Unauthorized");
            request.getSession().setAttribute("errors", "Login or password incorrect!");
            throw new CommandException("401 Unauthorized", null);

        }

        return "/profile";
    }

}
