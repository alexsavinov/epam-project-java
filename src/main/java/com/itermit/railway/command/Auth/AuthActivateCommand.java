package com.itermit.railway.command.Auth;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthActivateCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthActivateCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String activationToken = CommandContainer.getTokenFromRequest(request);

        logger.info("activationToken {}", activationToken);
            request.getSession().setAttribute("messages", "User activated!");

        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}
