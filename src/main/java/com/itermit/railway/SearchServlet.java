package com.itermit.railway;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import com.itermit.railway.dao.RouteDAO;
import com.itermit.railway.dao.StationDAO;
import com.itermit.railway.dao.UserRouteDAO;
import com.itermit.railway.dao.entity.Route;
import com.itermit.railway.dao.entity.Station;
import com.itermit.railway.dao.entity.UserRoute;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.dao.impl.UserRouteDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.utils.FilterQuery;
import com.itermit.railway.utils.Paginator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search", "/search/reserve/*"})
public class SearchServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SearchServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("doGet getPathInfo=" + request.getPathInfo());
        logger.info("doGet getRequestURI=" + request.getRequestURI());


        if (request.getRequestURI().equals("/search")) {
            /* [GET] SEARCH -- LIST */
            doGetSearch(request, response);
            logger.info("doGetSearch ->  " + request.getRequestURI());

        } else if (request.getRequestURI().startsWith("/search/reserve")) {
            /* [GET] SEARCH -- RESERVE */
            logger.info("doGetSearchReserve ->  " + request.getRequestURI());
            doGetReserve(request, response);

        } else {
            logger.info("doGetSearch UNHANDLED request.getRequestURI() -- " + request.getRequestURI());
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doPost getRequestURI -- " + request.getRequestURI());

        Boolean isAuthorized = false;
        if (request.getSession().getAttribute("isAuthorized") != null) {
            isAuthorized = (Boolean) request.getSession().getAttribute("isAuthorized");
        }

//        if (isAuthorized.equals(false)) {
//            /* [POST] NOT AUTHORISED */
//            logger.info("doPost is NOT Authorized -> auth.jsp");
//            request.getRequestDispatcher("/auth.jsp").forward(request, response);
//            return;
//        }

        if (request.getRequestURI().equals("/search")) {

            String action = request.getParameter("action");

            logger.info("doPost -> getParameter action: " + action);

            if (action != null && !action.isEmpty()) {
                if (action.equals("reset")) {
                    /* [POST] SEARCH -- RESET */
                    doPostReset(request, response);
                } else if (action.equals("search")) {
                    /* [POST] SEARCH -- SEARCH JSON */
                    doPostSearchJson(request, response);
                } else {
                    logger.info("doPost !!! FIXME !!! UNHANDLED action -- " + action);
                }
            } else {
                /* [POST] SEARCH -- SEARCH */
                doPostSearch(request, response);
            }

        } else if (request.getRequestURI().startsWith("/search/reserve/")) {
            /* [POST] SEARCH -- RESERVE */
            doPostReserve(request, response);
        } else {
            logger.info("doPost !!! FIXME !!! UNHANDLED getRequestURI() -- " + request.getRequestURI());
        }

    }

    /* GET */
    protected void doGetSearch(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doGetSearch isAuthorized -> search.jsp");

        HttpSession session = request.getSession();

        String dateRange = String.valueOf(session.getAttribute("daterange"));
        if (dateRange == null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

            String dateStart = dateTimeFormatter.format(LocalDateTime.now().minusMonths(1));
            String dateFinish = dateTimeFormatter.format(LocalDateTime.now().plusMonths(1));

            session.setAttribute("daterange", new StringBuilder(dateStart).append(" - ").append(dateFinish));
        }

        StationDAO stationDAO = new StationDAOImpl();
        try {
            ArrayList<Station> stations = stationDAO.getAll();

            session.setAttribute("stations", stations);
            stations.add(0, new Station(0, "-- Select --"));

            logger.info("doGetSearch stations -- " + stations);


        } catch (DBException e) {
            String errorString = e.toString();
            logger.info("doGetRoutesEdit SQLException -- " + errorString);

            session.setAttribute("url", "/search");
            session.setAttribute("errors", errorString);

            response.sendRedirect("/search");
            return;
        }

//        request.getRequestDispatcher("/reserve.jsp").forward(request, response);


        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }


    protected void doGetReserve(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doGetSearchReserve split [3] -- " + request.getRequestURI().split("/")[3]);
        logger.info("doGetSearchReserve -> routes.jsp");

        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        if (userid == null) {
//            request.getRequestDispatcher("/search.jsp").forward(request, response);
            return;
        }

//        String userid = (String) request.getSession().getAttribute("userid");
//        logger.info("doGetSearchReserve -- userid -- " + userid);

//        if (userid == null || userid.isEmpty()) {
//            logger.info("doGetSearchReserve is NOT Authorized -> auth.jsp");
//            request.getRequestDispatcher("/auth.jsp").forward(request, response);
//            return;
//        }

        int id = 0;

        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            id = Integer.parseInt(request.getPathInfo().substring(1));
        }

//        if (id == 0) {
//            logger.info("doGetSearchReserve -- Empty ID!");
////            request.getSession().setAttribute("errors", "Empty ID!");
//            request.getRequestDispatcher("/search.jsp").forward(request, response);
//            return;
//        }


        RouteDAO routeDAO = new RouteDAOImpl();
        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();

        try {
            Route route = routeDAO.get(id);

//            ArrayList<UserRoute> userRoutes = userRouteDAO.getAll();

            ArrayList<FilterQuery> filters = FilterQuery.getList();
            FilterQuery.addFilter(filters, "route_id", route.getId());
            ArrayList<UserRoute> userRoutesByRoute = userRouteDAO.getFiltered(filters);

            filters = FilterQuery.getList();
            FilterQuery.addFilter(filters, "user_id", userid);
            FilterQuery.addFilter(filters, "route_id", route.getId());
            ArrayList<UserRoute> userRoutesByCurrentUser = userRouteDAO.getFiltered(filters);

            request.setAttribute("route", route);
//            request.setAttribute("userRoutes", userRoutes);
            request.setAttribute("userRoutesByRoute", userRoutesByRoute);
            request.setAttribute("userRoutesByCurrentUser", userRoutesByCurrentUser);
//            request.setAttribute("action", "edit");

//            logger.info("doGetRoutesEdit userRoutes -- " + userRoutes);

//            request.getRequestDispatcher("/reserve.jsp").forward(request, response);
        } catch (DBException e) {
            String errorString = e.toString();
            logger.info("doGetRoutesEdit SQLException -- " + errorString);

            request.getSession().setAttribute("url", "/search");
            request.getSession().setAttribute("errors", errorString);

            response.sendRedirect("/search");
            return;
        }

        request.getRequestDispatcher("/reserve.jsp").forward(request, response);
    }

    /* POST */

    protected void doPostSearchJson(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doPostRoutesGetAll -- " + request.getRequestURI());

        RouteDAO routeDAO = new RouteDAOImpl();

//        try {
//            ArrayList<Route> routes = routeDAO.getAll();
//
//            GsonBuilder builder = new GsonBuilder();
//            Gson gson = builder.create();
//
//            Paginator paginator = new Paginator(1, 22, routes);
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//
//            logger.info("doPostSearchJson toJson paginator -- " + gson.toJson(paginator));
//            response.getWriter().write(gson.toJson(paginator));
//
//        } catch (DBException e) {
//            logger.info("doGet edit SQLException -- " + e);
//
//            response.sendRedirect("/search");
//        }
    }


    protected void doPostSearch(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doPostSearch -> search.jsp");

        logger.info("doPostSearch -> getParameter action " + request.getParameter("action"));


        ArrayList<FilterQuery> filters = FilterQuery.getList();


        logger.info("doPostSearch -> getParameter dateRange " + request.getParameter("daterange"));
        logger.info("doPostSearch -> getAttribute dateRange " + request.getSession().getAttribute("daterange"));

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
//            request.setAttribute("daterange", request.getParameter("daterange"));
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

        /* station_departure_id */
        String station_departure_id = request.getParameter("station_departure_id");

        if (station_departure_id != null && !station_departure_id.equals("0")) {
//            request.setAttribute("station_departure_id", station_departure_id);
            request.getSession().setAttribute("station_departure_id", station_departure_id);
            FilterQuery.addFilter(filters, "station_departure_id", "=", station_departure_id);
        } else {
            request.getSession().removeAttribute("station_departure_id");
        }

        /* station_arrival_id */
        String station_arrival_id = request.getParameter("station_arrival_id");
        if (station_arrival_id != null && !station_arrival_id.equals("0")) {
//            request.setAttribute("station_arrival_id", request.getParameter("station_arrival_id"));
            request.getSession().setAttribute("station_arrival_id", station_arrival_id);
            FilterQuery.addFilter(filters, "station_arrival_id", "=", station_arrival_id);
        } else {
            request.getSession().removeAttribute("station_arrival_id");
        }

        /* cost_min */
//        logger.info("doPostSearch -> getParameter cost_min " + request.getParameter("cost_min"));
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
//        logger.info("doPostSearch -> getParameter travel_time_min " + request.getParameter("travel_time_min"));
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

        try {
            StationDAO stationDAO = new StationDAOImpl();
            List<Station> stations = stationDAO.getAll();
            stations.add(0, new Station(0, "-- Select --"));

            request.getSession().setAttribute("stations", stations);

            RouteDAO routeDAO = new RouteDAOImpl();
            Paginator paginator = routeDAO.getFiltered(filters);

            request.getSession().setAttribute("routes", paginator.getData());
            request.getSession().setAttribute("paginator", paginator);
            logger.info("doPostSearch routes -- " + paginator.getData());
            logger.info("doPostSearch paginator -- " + paginator);

            response.sendRedirect("/search");

        } catch (DBException e) {
            String errorString = e.toString();
            logger.info("doPostSearch SQLException -- " + errorString);

            request.getSession().setAttribute("url", "/search");
            request.getSession().setAttribute("errors", errorString);

            response.sendRedirect("/search");
        }

    }

    protected void doPostReset(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doPostReset RESET");

        request.getSession().removeAttribute("daterange");
        request.getSession().setAttribute("station_departure_id", 0);
        request.getSession().setAttribute("station_arrival_id", 0);
        request.getSession().removeAttribute("cost_min");
        request.getSession().removeAttribute("cost_max");
        request.getSession().removeAttribute("min_seats");
        request.getSession().removeAttribute("travel_time_min");
        request.getSession().removeAttribute("travel_time_max");


//        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }

    protected void doPostReserve(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        int seats_reserve = Integer.parseInt(request.getParameter("seats_reserve"));
        int id = 0;

        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            id = Integer.parseInt(request.getPathInfo().substring(1));
        }

        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        if (userid == null) {
//            logger.info("doPostSearchReserve -- Empty ID!");
//            request.getSession().setAttribute("errors", "Empty ID!");
//            request.getRequestDispatcher("/search.jsp").forward(request, response);
            return;
        }

        logger.info("doPostSearchReserve seats_reserve -- " + seats_reserve);

//        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();
//        try {
//            UserRoute userRoute = new UserRoute(
//                    (int) request.getSession().getAttribute("userid"),
//                    id,
//                    seats_reserve);
//            userRouteDAO.add(userRoute);
//            request.getSession().setAttribute("messages", "Reserved seats: " + seats_reserve);
//
//        } catch (SQLException e) {
//            String errorString = e.toString();
//            logger.info("doPostSearchReserve SQLException -- " + errorString);
//
//            request.getSession().setAttribute("url", "/search");
//            request.getSession().setAttribute("errors", errorString);
//
//            response.sendRedirect("/search");
//            return;
//        }

        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }

}
