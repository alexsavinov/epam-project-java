package com.itermit.railway.command;

import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.utils.FilterQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ReservesListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReservesListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String userId = String.valueOf(request.getSession().getAttribute("userid"));
        if (userId != null) {
            logger.info("userId {}", userId);

            ArrayList<FilterQuery> filters = FilterQuery.getList();
            FilterQuery.addFilter(filters, "user_id", userId);
            ArrayList<Order> reserves = OrderDAOImpl.getInstance().getFiltered(filters);
            request.setAttribute("reserves", reserves);
        }

        try {
            request.getRequestDispatcher("/reserves.jsp").forward(request, response);
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
