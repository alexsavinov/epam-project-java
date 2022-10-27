package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.entity.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class OrdersListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrdersListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        try {
            ArrayList<Order> orders = OrderManager.getInstance().getAll();
            request.setAttribute("orders", orders);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        return "/orders.jsp";
    }

}
