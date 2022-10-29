package com.itermit.railway.command.User;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Command to add User.
 * <p>
 * Processes GET-Request.
 * Displays Form to submit.
 *
 * @author O.Savinov
 */
public class UserAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserAddGetCommand.class);

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

        request.setAttribute("action", "add");

        return "/user.jsp";
    }

}
