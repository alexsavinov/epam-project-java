package com.itermit.railway.command.Reserve;

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

/**
 * Command to get grouped by Route list of Reserves.
 * <p>
 * Processes GET-Request.
 * Displays grouped by Route list of Reserves.
 *
 * @author O.Savinov
 */
public class ReservesGroupedListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReservesGroupedListCommand.class);

    /**
     * Command execution.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Address string
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String userId = String.valueOf(request.getSession().getAttribute("userid"));
        if (userId != null) {
            try {
                ArrayList<Order> reserves = OrderManager.getInstance()
                        .getGroupedByRouteOfUser(Integer.parseInt(userId));
                request.getSession().setAttribute("reserves", reserves);
            } catch (DBException e) {
                logger.error("DBException. {}", e.getMessage());
                throw new CommandException(e.getMessage(), e);
            }
        }

        return "/reservesGrouped.jsp";
    }

}
