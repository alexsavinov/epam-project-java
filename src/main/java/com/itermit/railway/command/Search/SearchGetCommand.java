package com.itermit.railway.command.Search;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Command to Search for Routes with certain conditions.
 * <p>
 * Processes GET-Request.
 * Displays Form to submit.
 *
 * @author O.Savinov
 */
public class SearchGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchGetCommand.class);

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

        String dateRange = String.valueOf(session.getAttribute("daterange"));

        if (Objects.equals(dateRange, null) || dateRange.isEmpty()) {
            session.setAttribute("daterange", getDefaultDaterange());
        }

        try {
            ArrayList<Station> stations = StationManager.getInstance().getAll();
            stations.add(0, new Station.Builder().withName("-- Select --").build());
            session.setAttribute("stations", stations);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/search.jsp";
    }

    /**
     * Generates string with period for Start date and Finish date.
     * <p>
     * Fills Search form with default period.
     *
     * @return Date-range string.
     */
    public static String getDefaultDaterange() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

        String dateStart = dateTimeFormatter.format(LocalDateTime.now().minusMonths(1));
        String dateFinish = dateTimeFormatter.format(LocalDateTime.now().plusMonths(1));

        return new StringBuilder(dateStart).append(" - ").append(dateFinish).toString();

    }

}
