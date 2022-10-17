package com.itermit.railway.command;

import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RouteDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        RouteDAOImpl.getInstance().delete(id);

        request.getSession().setAttribute("messages", "Route deleted!");
        request.getSession().setAttribute("url", "/routes");
        request.setAttribute("action", "delete");

        try {
            response.sendRedirect("/routes");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting /routes!", e);
        }

        return null;
    }

}
