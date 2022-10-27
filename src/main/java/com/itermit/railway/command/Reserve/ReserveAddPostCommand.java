package com.itermit.railway.command.Reserve;

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

public class ReserveAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReserveAddPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int routeId = CommandContainer.getIntegerFromRequest(request, "route_id");
        int seats = CommandContainer.getIntegerFromRequest(request, "seats");
        int userId = (int) request.getSession().getAttribute("userid");

        Order order = new Order.Builder()
                .withUser(new User.Builder().withId(userId).build())
                .withRoute(new Route.Builder().withId(routeId).build())
                .withDefaultDateReserve().withSeats(seats).build();
        try {
            OrderManager.getInstance().add(order);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        CommandContainer.runCommand(request, response, "searchReset");

        request.getSession().setAttribute("messages", "Reserve for " + seats + " seats added!");

        return "/reserves/grouped";
    }

}
