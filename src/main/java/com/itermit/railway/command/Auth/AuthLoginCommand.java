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

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

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
            User userSearch = new User.Builder().withName(name).build();
            user = UserManager.getInstance().get(userSearch);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        StringBuilder sbErrors = new StringBuilder();

        if (user == null || user.getId() == 0) {
            logger.error("CommandException. User with such login does not exist.");
            request.getSession().setAttribute("errors", "User with such login does not exist!");
            sbErrors.append("User with such login does not exist!");
        } else if (!user.getPassword().equals(user.passwordEncrypt(password))) {
            logger.error("CommandException. Password incorrect.");
            request.getSession().setAttribute("errors", "Password incorrect!");
            sbErrors.append("Password incorrect!");
        } else if (!user.getIsActive()) {
            logger.error("CommandException. User is not yet activated.");
            request.getSession().setAttribute("errors", "User is not yet activated!");
            sbErrors.append("User is not yet activated!");
        }

        /* Display errors on the same page */
        if (sbErrors.length() > 0) {
            request.getSession().setAttribute("errors", sbErrors);
            return "/login";
        }

        logger.warn("User logged in: {}", user.getName());
        logger.warn("Password: {}", user.getPassword());
        logger.warn("IsActive: {}", user.getIsActive());

        request.getSession().setAttribute("isAuthorized", true);
        request.getSession().setAttribute("isAdmin", user.getIsAdmin());
        request.getSession().setAttribute("userid", user.getId());
        request.getSession().setAttribute("username", user.getName());
        request.getSession().setAttribute("email", user.getEmail());

        return "/profile";
    }

}
