package com.itermit.railway.command.Search;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.StationManager;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.utils.Condition;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SearchPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SearchPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        QueryMaker.Builder queryBuilder = new QueryMaker.Builder();

        /* Pagination - current page */
        String pgPage = request.getParameter("pg_page");
        if (pgPage == null || pgPage.equals("0")) {
            pgPage = "1";
        }
        request.getSession().setAttribute("pg_page", pgPage);
        queryBuilder.withPaginator(Integer.parseInt(pgPage));

        /* Filtering */
        processFiltering(request, queryBuilder, "daterange");
        processFiltering(request, queryBuilder, "train_number");
        processFiltering(request, queryBuilder, "station_departure_id");
        processFiltering(request, queryBuilder, "station_arrival_id");
        processFiltering(request, queryBuilder, "cost_min");
        processFiltering(request, queryBuilder, "cost_max");
        processFiltering(request, queryBuilder, "travel_time_min");
        processFiltering(request, queryBuilder, "travel_time_max");
        processFiltering(request, queryBuilder, "seats_available_min");
        processFiltering(request, queryBuilder, "seats_available_max");

        /* Sorting */
        processSorting(request, queryBuilder, "sort_train_number");
        processSorting(request, queryBuilder, "sort_station_departure");
        processSorting(request, queryBuilder, "sort_date_departure");
        processSorting(request, queryBuilder, "sort_station_arrival");
        processSorting(request, queryBuilder, "sort_date_arrival");
        processSorting(request, queryBuilder, "sort_travel_time");
        processSorting(request, queryBuilder, "sort_travel_cost");
        processSorting(request, queryBuilder, "sort_seats_available");
        processSorting(request, queryBuilder, "sort_seats_reserved");
        processSorting(request, queryBuilder, "sort_seats_total");

        try {
            List<Station> stations = StationManager.getInstance().getAll();
            stations.add(0, new Station.Builder().withName("-- Select --").build());

            request.getSession().setAttribute("stations", stations);

            Paginator paginator = RouteManager.getInstance().getPaginated(queryBuilder.build());

//        for (Route route: (ArrayList<Route>) paginator.getData()) {
//            logger.warn(route.getName(), route.getTravelTime());
//        }

            request.getSession().setAttribute("routes", paginator.getData());
            request.getSession().setAttribute("paginator", paginator);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        return "/search";
    }

    private static void processFiltering(HttpServletRequest request,
                                         QueryMaker.Builder queryBuilder,
                                         String parameterName) {

        String parameter = request.getParameter(parameterName);
        if (parameter != null && !parameter.isEmpty() && !parameter.equals("0")) {
            if (parameterName.equals("train_number")) {
                queryBuilder.withCondition(parameterName, Condition.LIKE, parameter);
            } else if (parameterName.equals("cost_min")) {
                parameterName = "travel_cost";
                queryBuilder.withCondition(parameterName, Condition.GE, parameter);
            } else if (parameterName.equals("cost_max")) {
                parameterName = "travel_cost";
                queryBuilder.withCondition(parameterName, Condition.LE, parameter);
            } else if (parameterName.equals("travel_time_min")) {
                parameterName = "travel_time";
                queryBuilder.withCondition(parameterName, Condition.GE, parameter);
            } else if (parameterName.equals("travel_time_max")) {
                parameterName = "travel_time";
                queryBuilder.withCondition(parameterName, Condition.LE, parameter);
            } else if (parameterName.equals("seats_available_min")) {
                parameterName = "seats_available";
                queryBuilder.withCondition(parameterName, Condition.GE, parameter);
            } else if (parameterName.equals("seats_available_max")) {
                parameterName = "seats_available";
                queryBuilder.withCondition(parameterName, Condition.LE, parameter);
            } else if (parameterName.equals("daterange")) {
                request.getSession().setAttribute(parameterName, parameter);
                String dateStart = new StringBuilder(parameter.substring(6, 10)).append("-")
                        .append(parameter, 3, 5).append("-")
                        .append(parameter, 0, 2).append(" 00:00:00").toString();
                String dateFinish = new StringBuilder(parameter.substring(19, 23)).append("-")
                        .append(parameter, 16, 18).append("-")
                        .append(parameter, 13, 15).append(" 23:59:59").toString();
                queryBuilder.withCondition("date_departure", Condition.GE, dateStart);
                queryBuilder.withCondition("date_arrival", Condition.LE, dateFinish);
            } else {
                queryBuilder.withCondition(parameterName, Condition.EQ, parameter);
            }
            request.getSession().setAttribute(parameterName, parameter);
        } else {
            request.getSession().removeAttribute(parameterName);
        }
    }

    private static void processSorting(HttpServletRequest request,
                                       QueryMaker.Builder queryBuilder,
                                       String parameterName) {

        String parameter = request.getParameter(parameterName);
        if (parameter != null && !parameter.isEmpty()) {
            request.getSession().setAttribute(parameterName, parameter);

            logger.trace("parameterName {}", parameterName);
            logger.trace("parameter {}", parameter);

            String fieldName = parameterName;
            if (parameterName.equals("sort_train_number")) {
                fieldName = "train_number";
            } else if (parameterName.equals("sort_station_departure")) {
                fieldName = "station_departure_name";
            } else if (parameterName.equals("sort_date_departure")) {
                fieldName = "date_departure";
            } else if (parameterName.equals("sort_station_arrival")) {
                fieldName = "station_arrival_name";
            } else if (parameterName.equals("sort_date_arrival")) {
                fieldName = "date_arrival";
            } else if (parameterName.equals("sort_travel_time")) {
                fieldName = "travel_time";
            } else if (parameterName.equals("sort_travel_cost")) {
                fieldName = "travel_cost";
            } else if (parameterName.equals("sort_seats_available")) {
                fieldName = "seats_available";
            } else if (parameterName.equals("sort_seats_reserved")) {
                fieldName = "seats_reserved";
            } else if (parameterName.equals("sort_seats_total")) {
                fieldName = "seats_total";
            }

            if (parameter.equals("asc")) {
                queryBuilder.withSort(fieldName, Condition.ASC);
            } else {
                queryBuilder.withSort(fieldName, Condition.DESC);
            }

        } else {
            request.getSession().removeAttribute(parameterName);
        }
    }

}
