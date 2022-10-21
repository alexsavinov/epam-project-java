package com.itermit.railway.command.Search;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class SearchGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        HttpSession session = request.getSession();

        String dateRange = String.valueOf(session.getAttribute("daterange"));
        if (dateRange == null) {
            session.setAttribute("daterange", getDefaultDaterange());
        }

        try {
            ArrayList<Station> stations = StationManager.getInstance().getAll();
            stations.add(0, new Station.Builder().withName("-- Select --").build());
            session.setAttribute("stations", stations);

            request.getRequestDispatcher("/search.jsp").forward(request, response);

        } catch (ServletException e) {
            logger.error("ServletException. Error searching! {}", e.getMessage());
            throw new DBException("Error searching!", e);
        } catch (IOException e) {
            logger.error("IOException. Error searching! {}", e.getMessage());
            throw new DBException("Error searching!", e);
        }
        return null;
    }

    public static String getDefaultDaterange() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

        String dateStart = dateTimeFormatter.format(LocalDateTime.now().minusMonths(1));
        String dateFinish = dateTimeFormatter.format(LocalDateTime.now().plusMonths(1));

        return new StringBuilder(dateStart).append(" - ").append(dateFinish).toString();

    }

}
