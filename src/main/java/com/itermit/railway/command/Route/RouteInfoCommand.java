package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class RouteInfoCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteInfoCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        logger.info("id = {}", id);

        Route route = RouteManager.getInstance().get(id);

        request.getSession().setAttribute("route", route);
//        request.getSession().setAttribute("messages", "Reserve deleted!");

        QueryMaker query = new QueryMaker.Builder()
                .withCondition("route_id", Condition.EQ , String.valueOf(id))
                .build();

        Paginator paginator = OrderManager.getInstance().getPaginated(query);

//            for (Route route: (ArrayList<Route>) paginator.getData()) {
//                logger.warn(route.getName(), route.getTravelTime());
//            }

        request.getSession().setAttribute("orders", paginator.getData());
        request.getSession().setAttribute("paginator", paginator);

        try {
            request.getRequestDispatcher("/routeInfo.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error route editing! {}", e.getMessage());
            throw new DBException("Error route editing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error route editing! {}", e.getMessage());
            throw new DBException("Error route editing!", e);
        }

        return null;
    }

}
