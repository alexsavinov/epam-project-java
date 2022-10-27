package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        try {
            OrderManager.getInstance().delete(id);
            request.getSession().setAttribute("messages", "Order deleted!");
            request.getSession().setAttribute("url", "/profile");
            request.setAttribute("action", "delete");
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/orders";
    }

}
