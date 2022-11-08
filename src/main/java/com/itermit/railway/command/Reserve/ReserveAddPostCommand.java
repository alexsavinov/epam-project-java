package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Command to add Reserve.
 * <p>
 * Processes POST-Request.
 * Adds new Reserve to database.
 *
 * @author O.Savinov
 */
public class ReserveAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReserveAddPostCommand.class);

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

        int routeId = CommandContainer.getIntegerFromRequest(request, "route_id");
        int seats = CommandContainer.getIntegerFromRequest(request, "seats");

        String userIdString = String.valueOf(request.getSession().getAttribute("userid"));
        int userId;
        if (userIdString == null) {
            userId = 0;
        } else {
            userId = Integer.parseInt(userIdString);

        }

        Order order = new Order.Builder()
                .withUser(new User.Builder().withId(userId).build())
                .withRoute(new Route.Builder().withId(routeId).build())
                .withDefaultDateReserve().withSeats(seats).build();
        try {
            OrderManager.getInstance().add(order);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        CommandContainer.runCommand(request, response, "searchReset");

        HashMap<String, String> localizedMessages = new HashMap<>();
        localizedMessages.put("en", "Reserve for " + seats + " seats added!");
        localizedMessages.put("uk", "Бронювання для " + seats + " місць додане!");
        request.getSession().setAttribute("localizedMessages", localizedMessages);

        request.getSession().removeAttribute("paginator");

        return "/reserves/grouped";
    }

}
