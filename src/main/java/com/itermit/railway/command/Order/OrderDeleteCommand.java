package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        OrderManager.getInstance().delete(id);

        request.getSession().setAttribute("messages", "Order deleted!");
        request.getSession().setAttribute("url", "/profile");
        request.setAttribute("action", "delete");

        try {
            response.sendRedirect("/orders");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting /orders!", e);
        }

        return null;
    }

}
