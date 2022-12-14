package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Command to get list of Reserves.
 * <p>
 * Processes GET-Request.
 * Displays list of Reserves.
 *
 * @author O.Savinov
 */
public class ReservesListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReservesListCommand.class);

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
                .withCondition("user_id", Condition.EQ, String.valueOf(request.getSession().getAttribute("userid")))
                .withCondition("route_id", Condition.EQ, String.valueOf(id))
                .build();

        try {
            ArrayList<Order> reserves = OrderManager.getInstance().getFiltered(query);
            request.getSession().setAttribute("reserves", reserves);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/reservesGrouped.jsp";
    }

}
