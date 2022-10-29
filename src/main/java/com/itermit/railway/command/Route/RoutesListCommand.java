package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.entity.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Command to get list of Routes.
 * <p>
 * Processes GET-Request.
 * Displays list of Routes.
 *
 * @author O.Savinov
 */
public class RoutesListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RoutesListCommand.class);

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

        ArrayList<Route> routes = null;
        try {
            routes = RouteManager.getInstance().getAll();
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }
        request.setAttribute("routes", routes);

        return "/routes.jsp";
    }

}
