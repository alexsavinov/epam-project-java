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

/**
 * Command to edit Order.
 * <p>
 * Processes POST-Request.
 * Updates existed Order in database.
 *
 * @author O.Savinov
 */
public class OrderEditPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderEditPostCommand.class);

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
        User user = new User.Builder()
                .withId(Integer.parseInt(request.getParameter("user"))).build();
        int routeId =  Integer.parseInt(request.getParameter("route"));
        Route route = new Route.Builder().withId(routeId).build();
        Order order = new Order.Builder()
                .withUser(user)
                .withRoute(route)
                .withSeats(CommandContainer.getIntegerFromRequest(request, "seats"))
                .withDateReserve(CommandContainer.getStringFromRequest(request, "date_reserve"))
                .build();
        try {
            OrderManager.getInstance().update(id, order);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        request.getSession().setAttribute("messages", "Order updated!");
        request.getSession().setAttribute("url", "/orders");

        return "/orders/edit/" + id;
    }

}
