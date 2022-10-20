package com.itermit.railway.command.Search;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.itermit.railway.command.Search.SearchGetCommand.getDefaultDaterange;

public class SearchResetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchResetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        HttpSession session = request.getSession();

        session.setAttribute("daterange", getDefaultDaterange());
        session.removeAttribute("train_number");
        session.setAttribute("station_departure_id", 0);
        session.setAttribute("station_arrival_id", 0);
        session.removeAttribute("cost_min");
        session.removeAttribute("cost_max");
        session.removeAttribute("min_seats");
        session.removeAttribute("travel_time_min");
        session.removeAttribute("travel_time_max");
        session.removeAttribute("seats_available_min");
        session.removeAttribute("seats_available_max");
        session.removeAttribute("orders");
        session.removeAttribute("routes");

        return null;

    }

}
