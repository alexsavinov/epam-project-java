package com.itermit.railway.command.Reserve;

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

public class ReservesGroupedListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReservesGroupedListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String userId = String.valueOf(request.getSession().getAttribute("userid"));
        if (userId != null) {
            ArrayList<Order> reserves = OrderManager.getInstance()
                    .getGroupedByRouteOfUser(Integer.parseInt(userId));
            request.getSession().setAttribute("reserves", reserves);
        }

        try {
            request.getRequestDispatcher("/reservesGrouped.jsp").forward(request, response);
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
