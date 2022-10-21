package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.User;
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

public class ReserveEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReserveEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

//        ArrayList<User> users = UserManager.getInstance().getAll();
//        ArrayList<Route> routes = RouteManager.getInstance().getAll();
//        Order order = OrderManager.getInstance().get(id);
//
//        request.setAttribute("users", users);
//        request.setAttribute("routes", routes);
//        request.setAttribute("order", order);
//        request.setAttribute("action", "edit");


//                int id = CommandContainer.getIdFromRequest(request);

//        int id =  Integer.parseInt(request.getParameter("order_id"));
//        int seats =  Integer.parseInt(request.getParameter("seats"));

        logger.info("id = {}", id);

        Order order = OrderManager.getInstance().get(id);

        request.getSession().setAttribute("order", order);
//        request.getSession().setAttribute("messages", "Reserve deleted!");

        logger.info("id = {}", id);

        QueryMaker query = new QueryMaker.Builder()
                .withCondition("user_id", Condition.EQ , String.valueOf(order.getUser().getId()))
                .withCondition("route_id", Condition.EQ , String.valueOf(order.getRoute().getId()))
                .build();

        ArrayList<Order> reserves = OrderManager.getInstance().getFiltered(query);
        request.getSession().setAttribute("reserves", reserves);
//        ArrayList<Order> tickets = OrderManager.getInstance().getFiltered(query);
//        ArrayList<Order> tickets = OrderManager.getInstance().getGroupedByRouteOfUser(order.getUser().getId());

//          TODO Generate grate tickets iterating thru the count of reserved seats.

//        request.getSession().setAttribute("tickets", tickets);

//            for (Route route: (ArrayList<Route>) paginator.getData()) {
//                logger.warn(route.getName(), route.getTravelTime());
//            }

        try {
            request.getRequestDispatcher("/reserve.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error editing reserve! {}", e.getMessage());
            throw new DBException("Error editing reserve!", e);
        } catch (IOException e) {
            logger.error("IOException. Error editing reserve! {}", e.getMessage());
            throw new DBException("Error editing reserve!", e);
        }

        return null;
    }

}
