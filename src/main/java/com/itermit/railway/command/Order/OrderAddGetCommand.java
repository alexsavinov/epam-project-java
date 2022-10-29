package com.itermit.railway.command.Order;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to add Order.
 * <p>
 * Processes GET-Request.
 * Displays Form to submit.
 *
 * @author O.Savinov
 */
public class OrderAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(OrderAddGetCommand.class);

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

        try {
            request.setAttribute("users", UserManager.getInstance().getAll());
            request.setAttribute("routes", RouteManager.getInstance().getAll());
            request.setAttribute("action", "add");
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/order.jsp";
    }

}
