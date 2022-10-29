package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to delete Route.
 * <p>
 * Processes POST-Request.
 * Deletes Route from database.
 *
 * @author O.Savinov
 */
public class RouteDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteDeleteCommand.class);

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
            RouteManager.getInstance().delete(id);
            request.getSession().setAttribute("messages", "Route deleted!");
            request.getSession().setAttribute("url", "/routes");
            request.setAttribute("action", "delete");
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        return "/routes";
    }

}
