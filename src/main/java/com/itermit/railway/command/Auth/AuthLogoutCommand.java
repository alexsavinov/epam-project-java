package com.itermit.railway.command.Auth;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to log out User.
 *
 * @author O.Savinov
 */
public class AuthLogoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthLogoutCommand.class);

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

        request.getSession().removeAttribute("isAuthorized");
        request.getSession().removeAttribute("userid");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("setIsAdmin");
        request.getSession().removeAttribute("email");

        return "/";
    }

}
