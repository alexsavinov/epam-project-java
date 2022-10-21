package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.entity.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class OrdersListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrdersListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        ArrayList<Order> orders = OrderManager.getInstance().getAll();
        request.setAttribute("orders", orders);

        try {
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error stations listing! {}", e.getMessage());
            throw new DBException("Error stations listing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error stations listing! {}", e.getMessage());
            throw new DBException("Error stations listing!", e);
        }

        return null;
    }

}
