package com.itermit.railway.command.Search;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Arrays;

import static com.itermit.railway.command.Search.SearchGetCommand.getDefaultDaterange;

/**
 * Command to reset Search conditions and results.
 * <p>
 * Processes POST-Request.
 * Clears all manual setting for Search.
 *
 * @author O.Savinov
 */
public class SearchResetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchResetCommand.class);

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

        HttpSession session = request.getSession();

        session.setAttribute("daterange", getDefaultDaterange());
        session.removeAttribute("train_number");
        session.setAttribute("station_departure_id", 0);
        session.setAttribute("station_arrival_id", 0);
        for (String s : Arrays.asList("cost_min", "cost_max", "min_seats", "travel_time_min",
                "travel_time_max", "seats_available_min", "seats_available_max", "orders", "routes",
                "paginator", "sort_train_number", "sort_station_departure", "sort_date_departure",
                "sort_travel_time", "sort_station_arrival", "sort_date_arrival", "sort_travel_cost",
                "sort_seats_reserved", "sort_seats_available", "sort_seats_total")) {
            session.removeAttribute(s);
        }

        return null;
    }

}
