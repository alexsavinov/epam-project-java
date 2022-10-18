package com.itermit.railway.command;

import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReserveAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReserveAddPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

//        String route_id = request.getParameter("route_id");
        int route_id = CommandContainer.getIntegerFromRequest(request, "route_id");
        int seats = CommandContainer.getIntegerFromRequest(request, "seats");

        logger.info("seats {}", seats);
        logger.info("route_id {}", route_id);
        logger.info("user_id {}", request.getSession().getAttribute("userid"));
        int user_id = (int) request.getSession().getAttribute("userid");


        Route route = RouteDAOImpl.getInstance().get(route_id);

        logger.info("route {}", route);

        Order order = new Order.Builder()
                .withUser(new User.Builder().withId(user_id).build())
                .withRoute(new Route.Builder().withId(route_id).build())
                .withDefaultDateReserve().withSeats(seats).build();

        logger.info("order {}", order);
        OrderDAOImpl.getInstance().add(order);

        CommandContainer.runCommand(request, response, "searchReset");

        request.getSession().setAttribute("messages", "Reserve deleted!");

        try {
            response.sendRedirect("/reserves");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}
