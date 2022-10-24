package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.UserManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class OrderEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        ArrayList<User> users = UserManager.getInstance().getAll();
        ArrayList<Route> routes = RouteManager.getInstance().getAll();
        Order order = OrderManager.getInstance().get(id);

        request.setAttribute("users", users);
        request.setAttribute("routes", routes);
        request.setAttribute("order", order);
        request.setAttribute("action", "edit");

        try {
            request.getRequestDispatcher("/order.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error editing order! {}", e.getMessage());
            throw new DBException("Error editing order!", e);
        } catch (IOException e) {
            logger.error("IOException. Error editing order! {}", e.getMessage());
            throw new DBException("Error editing order!", e);
        }

        return null;
    }

}
