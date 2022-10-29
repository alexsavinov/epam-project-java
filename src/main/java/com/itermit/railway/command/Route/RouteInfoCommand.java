package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.*;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to get Route information page.
 * <p>
 * Processes GET-Request.
 * Also, displays list of related Routes.
 *
 * @author O.Savinov
 */
public class RouteInfoCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteInfoCommand.class);

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
            Route route = RouteManager.getInstance().get(id);
            request.getSession().setAttribute("route", route);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        QueryMaker query = new QueryMaker.Builder()
                .withCondition("route_id", Condition.EQ, String.valueOf(id))
                .build();

        try {
            Paginator paginator = OrderManager.getInstance().getPaginated(query);
            request.getSession().setAttribute("orders", paginator.getData());
            request.getSession().setAttribute("paginator", paginator);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/routeInfo.jsp";
    }

}
