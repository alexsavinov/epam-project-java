package com.itermit.railway.command;

import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.utils.FilterQuery;
import com.itermit.railway.utils.Paginator;
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

        ArrayList<FilterQuery> filters = FilterQuery.getList();

        /* min_seats -- should be 1st filter */
        String min_seats = request.getParameter("min_seats");
        if (min_seats != null && !min_seats.equals("0") && !min_seats.isEmpty()) {
            request.getSession().setAttribute("min_seats", request.getParameter("min_seats"));
            FilterQuery.addFilter(filters, "seats_available", ">=", min_seats);
        } else {
            request.getSession().removeAttribute("min_seats");
        }

        /* pg_page  - pagination current page */
        //        pg_page, pg_page_prev, pg_page_next, pg_pages_total
        String pg_page = request.getParameter("pg_page");
        logger.info("doPostSearch -> getParameter pg_page " + pg_page);
        if (pg_page != null && !pg_page.equals("0")) {
//            pg_page = 1;
        } else {
            pg_page = "1";
        }
        request.getSession().setAttribute("pg_page", pg_page);
        FilterQuery.addFilter(filters, "pg_page", pg_page);


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

            FilterQuery.addFilter(filters, "date_departure", ">=", dateStart);
            FilterQuery.addFilter(filters, "date_arrival", "<=", dateFinish);
        }

        /* train_number */
        String train_number = request.getParameter("train_number");
        if (train_number != null && !train_number.isEmpty()) {
            request.getSession().setAttribute("train_number", train_number);
            FilterQuery.addFilter(filters, "train_number", "=", train_number);
        } else {
            request.getSession().removeAttribute("train_number");
        }

        /* station_departure_id */
        String station_departure_id = request.getParameter("station_departure_id");

        if (station_departure_id != null && !station_departure_id.equals("0")) {
            request.getSession().setAttribute("station_departure_id", station_departure_id);
            FilterQuery.addFilter(filters, "station_departure_id", "=", station_departure_id);
        } else {
            request.getSession().removeAttribute("station_departure_id");
        }

        /* station_arrival_id */
        String station_arrival_id = request.getParameter("station_arrival_id");
        if (station_arrival_id != null && !station_arrival_id.equals("0")) {
            request.getSession().setAttribute("station_arrival_id", station_arrival_id);
            FilterQuery.addFilter(filters, "station_arrival_id", "=", station_arrival_id);
        } else {
            request.getSession().removeAttribute("station_arrival_id");
        }

        /* cost_min */
        String cost_min = request.getParameter("cost_min");
        if (cost_min != null && !cost_min.equals("0") && !cost_min.isEmpty()) {
            request.getSession().setAttribute("cost_min", request.getParameter("cost_min"));
            FilterQuery.addFilter(filters, "travel_cost", ">=", cost_min);
        } else {
            request.getSession().removeAttribute("cost_min");
        }

        /* cost_max */
        String cost_max = request.getParameter("cost_max");
        if (cost_max != null && !cost_max.equals("0") && !cost_max.isEmpty()) {
            request.getSession().setAttribute("cost_max", request.getParameter("cost_max"));
            FilterQuery.addFilter(filters, "travel_cost", "<=", cost_max);
        } else {
            request.getSession().removeAttribute("cost_max");
        }

        /* travel_time_min */
        String travel_time_min = request.getParameter("travel_time_min");
        if (travel_time_min != null && !travel_time_min.equals("0") && !travel_time_min.isEmpty()) {
            request.getSession().setAttribute("travel_time_min", request.getParameter("travel_time_min"));
            FilterQuery.addFilter(filters, "travel_time", ">=", travel_time_min);
        } else {
            request.getSession().removeAttribute("travel_time_min");
        }

        /* travel_time_max */
        String travel_time_max = request.getParameter("travel_time_max");
        if (travel_time_max != null && !travel_time_max.equals("0") && !travel_time_max.isEmpty()) {
            request.getSession().setAttribute("travel_time_max", request.getParameter("travel_time_max"));
            FilterQuery.addFilter(filters, "travel_time", "<=", travel_time_max);
        } else {
            request.getSession().removeAttribute("travel_time_max");
        }

        List<Station> stations = StationDAOImpl.getInstance().getAll();
        stations.add(0, new Station.Builder().withName("-- Select --").build());

        request.getSession().setAttribute("stations", stations);

        Paginator paginator = RouteDAOImpl.getInstance().getFiltered(filters);

        for (Route route: (ArrayList<Route>) paginator.getData()) {
            logger.warn(route.getName(), route.getTravelTime());
        }

        request.getSession().setAttribute("routes", paginator.getData());
        request.getSession().setAttribute("paginator", paginator);
//        logger.info("doPostSearch routes -- " + paginator.getData());
//        logger.info("doPostSearch paginator -- " + paginator);

        try {
            response.sendRedirect("/search");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting /search! {}", e.getMessage());
            throw new DBException("Error redirecting /search!", e);
        }

        return null;
    }


}
