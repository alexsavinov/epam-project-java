package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Command to add Route.
 * <p>
 * Processes GET-Request.
 * Displays Form to submit.
 *
 * @author O.Savinov
 */
public class RouteAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteAddGetCommand.class);

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

        try {
            ArrayList<Station> stations = StationManager.getInstance().getAll();
            request.setAttribute("stations", stations);
            request.setAttribute("action", "add");
            request.getSession().setAttribute("action1", "add1");
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/route.jsp";
    }

}
