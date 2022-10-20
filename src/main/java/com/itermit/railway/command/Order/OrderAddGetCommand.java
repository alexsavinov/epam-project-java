package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderAddGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        request.setAttribute("users", UserDAOImpl.getInstance().getAll());
        request.setAttribute("routes", RouteDAOImpl.getInstance().getAll());
        request.setAttribute("action", "add");

        try {
            request.getRequestDispatcher("/order.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error adding order! {}", e.getMessage());
            throw new DBException("Error adding order!", e);
        } catch (IOException e) {
            logger.error("IOException. Error adding order! {}", e.getMessage());
            throw new DBException("Error adding order!", e);
        }

        return null;
    }

}
