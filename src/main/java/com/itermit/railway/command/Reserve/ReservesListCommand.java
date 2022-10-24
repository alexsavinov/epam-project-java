package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.QueryMaker;
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

//        String userId = String.valueOf(request.getSession().getAttribute("userid"));

        int id = CommandContainer.getIdFromRequest(request);

        Route route = RouteManager.getInstance().get(id);

        request.getSession().setAttribute("route", route);

        QueryMaker query = new QueryMaker.Builder()
                .withCondition("user_id", Condition.EQ , String.valueOf(request.getSession().getAttribute("userid")))
                .withCondition("route_id", Condition.EQ , String.valueOf(id))
                .build();

        ArrayList<Order> reserves = OrderManager.getInstance().getFiltered(query);
        request.getSession().setAttribute("reserves", reserves);

        logger.warn(reserves);


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
