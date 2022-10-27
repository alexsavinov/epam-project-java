package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class ReserveDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReserveDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id =  Integer.parseInt(request.getParameter("order_id"));
        int seats =  Integer.parseInt(request.getParameter("seats"));

        try {
            OrderManager.getInstance().deleteReserve(id, seats);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        request.getSession().setAttribute("messages",
                String.format("Reserve for %d seats removed (order ID: %d)!", seats, id));

        int routeId =  Integer.parseInt(request.getParameter("route_id"));
        QueryMaker query = new QueryMaker.Builder()
                .withCondition("user_id", Condition.EQ , String.valueOf(request.getSession().getAttribute("userid")))
                .withCondition("route_id", Condition.EQ , String.valueOf(routeId))
                .build();

        try {
            ArrayList<Order> reserves = OrderManager.getInstance().getFiltered(query);
            request.getSession().setAttribute("reserves", reserves);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/reserve.jsp";
    }

}
