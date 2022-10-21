package com.itermit.railway.command.Search;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        QueryMaker.Builder queryBuilder = new QueryMaker.Builder();

        /* pg_page - pagination current page */
        String pg_page = request.getParameter("pg_page");
        if (pg_page != null && !pg_page.equals("0")) {
        } else {
            pg_page = "1";
        }
        request.getSession().setAttribute("pg_page", pg_page);
        queryBuilder.withPaginator(Integer.parseInt(pg_page));

        /* daterange */
        String dateRange = request.getParameter("daterange");
        if (dateRange != null && !dateRange.isEmpty()) {
            request.getSession().setAttribute("daterange", dateRange);
            String dateStart = new StringBuilder(dateRange.substring(6, 10)).append("-")
                    .append(dateRange, 3, 5).append("-")
                    .append(dateRange, 0, 2).append(" 00:00:00").toString();
            String dateFinish = new StringBuilder(dateRange.substring(19, 23)).append("-")
                    .append(dateRange, 16, 18).append("-")
                    .append(dateRange, 13, 15).append(" 23:59:59").toString();
            queryBuilder.withCondition("date_departure", Condition.GE, dateStart);
            queryBuilder.withCondition("date_arrival", Condition.LE, dateFinish);
        }

        /* train_number */
        String train_number = request.getParameter("train_number");
        if (train_number != null && !train_number.isEmpty()) {
            request.getSession().setAttribute("train_number", train_number);
            queryBuilder.withCondition("train_number", Condition.LIKE, train_number);
        } else {
            request.getSession().removeAttribute("train_number");
        }

        /* station_departure_id */
        String station_departure_id = request.getParameter("station_departure_id");
        if (station_departure_id != null && !station_departure_id.equals("0")) {
            request.getSession().setAttribute("station_departure_id", station_departure_id);
            queryBuilder.withCondition("station_departure_id", Condition.EQ, station_departure_id);
        } else {
            request.getSession().removeAttribute("station_departure_id");
        }

        /* station_arrival_id */
        String station_arrival_id = request.getParameter("station_arrival_id");
        if (station_arrival_id != null && !station_arrival_id.equals("0")) {
            request.getSession().setAttribute("station_arrival_id", station_arrival_id);
            queryBuilder.withCondition("station_arrival_id", Condition.EQ, station_arrival_id);
        } else {
            request.getSession().removeAttribute("station_arrival_id");
        }

        /* cost_min */
        String cost_min = request.getParameter("cost_min");
        if (cost_min != null && !cost_min.equals("0") && !cost_min.isEmpty()) {
            request.getSession().setAttribute("cost_min", cost_min);
            queryBuilder.withCondition("travel_cost", Condition.GE, cost_min);
        } else {
            request.getSession().removeAttribute("cost_min");
        }

        /* cost_max */
        String cost_max = request.getParameter("cost_max");
        if (cost_max != null && !cost_max.equals("0") && !cost_max.isEmpty()) {
            request.getSession().setAttribute("cost_max", cost_max);
            queryBuilder.withCondition("travel_cost", Condition.LE, cost_max);
        } else {
            request.getSession().removeAttribute("cost_max");
        }

        /* travel_time_min */
        String travel_time_min = request.getParameter("travel_time_min");
        if (travel_time_min != null && !travel_time_min.equals("0") && !travel_time_min.isEmpty()) {
            request.getSession().setAttribute("travel_time_min", travel_time_min);
            queryBuilder.withCondition("travel_time", Condition.GE, travel_time_min);
        } else {
            request.getSession().removeAttribute("travel_time_min");
        }

        /* travel_time_max */
        String travel_time_max = request.getParameter("travel_time_max");
        if (travel_time_max != null && !travel_time_max.equals("0") && !travel_time_max.isEmpty()) {
            request.getSession().setAttribute("travel_time_max", travel_time_max);
            queryBuilder.withCondition("travel_time", Condition.LE, travel_time_max);
        } else {
            request.getSession().removeAttribute("travel_time_max");
        }

        /* seats_available_min */
        String seats_available_min = request.getParameter("seats_available_min");
        if (seats_available_min != null && !seats_available_min.equals("0") && !seats_available_min.isEmpty()) {
            request.getSession().setAttribute("seats_available_min", seats_available_min);
            queryBuilder.withCondition("seats_available", Condition.GE, seats_available_min);
        } else {
            request.getSession().removeAttribute("seats_available_min");
        }

        /* seats_available_max */
        String seats_available_max = request.getParameter("seats_available_max");
        if (seats_available_max != null && !seats_available_max.equals("0") && !seats_available_max.isEmpty()) {
            request.getSession().setAttribute("seats_available_max", seats_available_max);
            queryBuilder.withCondition("seats_available", Condition.LE, seats_available_max);
        } else {
            request.getSession().removeAttribute("seats_available_max");
        }

        /* SORTING */

        /* sort_train_number */
        String sort_train_number = request.getParameter("sort_train_number");
        logger.info("sort_train_number: {}", sort_train_number);
        if (sort_train_number != null && !sort_train_number.isEmpty()) {

            if (sort_train_number.equals("asc")) {
                queryBuilder.withSort("train_number", Condition.ASC);
            } else {
                queryBuilder.withSort("train_number", Condition.DESC);
            }
            request.getSession().setAttribute("sort_train_number", sort_train_number);

        } else {
            request.getSession().removeAttribute("sort_train_number");
        }

        List<Station> stations = StationManager.getInstance().getAll();
        stations.add(0, new Station.Builder().withName("-- Select --").build());

        request.getSession().setAttribute("stations", stations);

        Paginator paginator = RouteManager.getInstance().getPaginated(queryBuilder.build());

//        for (Route route: (ArrayList<Route>) paginator.getData()) {
//            logger.warn(route.getName(), route.getTravelTime());
//        }

        request.getSession().setAttribute("routes", paginator.getData());
        request.getSession().setAttribute("paginator", paginator);

        try {
            response.sendRedirect("/search");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting /search! {}", e.getMessage());
            throw new DBException("Error redirecting /search!", e);
        }

        return null;
    }


}
