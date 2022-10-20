package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.FilterQuery;
import com.itermit.railway.utils.Paginator;
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

        String userId = String.valueOf(request.getSession().getAttribute("userid"));
        if (userId != null) {
            logger.info("userId {}", userId);

            QueryMaker query = new QueryMaker.Builder()
                    .withCondition("user_id", Condition.EQ ,userId).build();

//            logger.info("QueryMaker query = {}", query);
//            logger.info("Condition EQ = {}", Condition.EQ.get());
//            logger.info("user_id, Condition.EQ ,userId = {} {} {}", "user_id", Condition.EQ.get() ,userId);


//            ArrayList<FilterQuery> filters = FilterQuery.getList();
//            FilterQuery.addFilter(filters, "user_id", userId);
////            ArrayList<Order> reserves = OrderDAOImpl.getInstance().getFiltered1(filters);
//            ArrayList<Order> reserves = OrderDAOImpl.getInstance().getFiltered1(query);
//            request.setAttribute("reserves", reserves);



            Paginator paginator = OrderDAOImpl.getInstance().getPaginated(query);

//            for (Route route: (ArrayList<Route>) paginator.getData()) {
//                logger.warn(route.getName(), route.getTravelTime());
//            }

            request.getSession().setAttribute("reserves", paginator.getData());

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
