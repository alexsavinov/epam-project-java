package com.itermit.railway.command.Auth;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthLogoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AuthLogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String username = (String) request.getSession().getAttribute("username");
        request.getSession().removeAttribute("isAuthorized");
        request.getSession().removeAttribute("userid");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("setIsAdmin");
        request.getSession().removeAttribute("email");

        try {
            logger.warn("User logged out: {}", username);
            response.sendRedirect("/");
        } catch (IOException e) {
            logger.error("IOException. Error during redirect!  {}", e.getMessage());
			throw new DBException("Error redirect to page!", e);
        }

        return null;
    }

}
