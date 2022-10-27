package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderAddPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        User user = new User.Builder()
                .withId(Integer.parseInt(request.getParameter("user"))).build();

        int routeId = Integer.parseInt(request.getParameter("route"));

        Route route = new Route.Builder().withId(routeId).build();

        Order order = new Order.Builder()
                .withUser(user)
                .withRoute(route)
                .withSeats(CommandContainer.getIntegerFromRequest(request, "seats"))
                .withDateReserve(CommandContainer.getStringFromRequest(request, "date_reserve"))
                .build();

        try {
            OrderManager.getInstance().add(order);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        request.getSession().setAttribute("messages", "Order added!");

        return "/orders";
    }

}
