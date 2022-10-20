package com.itermit.railway.controller;


import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.utils.FilterQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "RouteServlet",
        urlPatterns = {"/routes", "/routes/delete/*", "/routes/edit/*", "/routes/add"})
public class RouteServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RouteServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().equals("/routes")) {

            commandName = "routesList";

        } else if (request.getRequestURI().contains("/routes/edit")) {

            commandName = "routeEditGet";

        } else if (request.getRequestURI().contains("/routes/add")) {

            commandName = "routeAddGet";

        } else if (request.getRequestURI().contains("/routes/delete")) {

            doPost(request, response);

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());

        String commandName = null;

        if (request.getRequestURI().startsWith("/routes/edit")) {

            commandName = "routeEditPost";

        } else if (request.getRequestURI().equals("/routes/add")) {

            commandName = "routeAddPost";

        } else if (request.getRequestURI().startsWith("/routes/delete")) {

            commandName = "routeDelete";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);
            logger.error("UNHANDLED request!  {}", request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);
    }


    /* POST /routes/getAll */
//    protected void doPostRoutesGetAll(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        logger.info("doPostRoutesGetAll -- " + request.getRequestURI());
//
//
//        try {
//            ArrayList<Route> routes = RouteDAOImpl.getInstance().getAll();
//
//            GsonBuilder builder = new GsonBuilder();
//            Gson gson = builder.create();
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//
//            logger.info("doPostRoutesGetAll toJson -- " + gson.toJson(routes));
//            response.getWriter().write(gson.toJson(routes));
//        } catch (SQLException e) {
//            logger.info("doGet edit SQLException -- " + e);
//
//            response.sendRedirect("/routes");
//        }
//    }

    /* POST /routes/getById/:id */
//    protected void doPostRoutesGetById(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        logger.info("doPostRoutesGet -- " + request.getRequestURI());
//        logger.info("doPostRoutesGet split [3] -- " + request.getRequestURI().split("/")[3]);
//
//        // TODO: Передавать в теле запроса айдишку, а не через URL. Это же POST запрос.
//
//        int id = Integer.parseInt(request.getRequestURI().split("/")[3]);
//
//        ArrayList<Station> stations = new ArrayList<>();
//
//        try {
//            stations = StationDAOImpl.getInstance().getAll();
//            Route route = RouteDAOImpl.getInstance().get(id);
////            UserRoute userRoute = userRouteDAO.get(id);
////            request.setAttribute("route", route);
////            request.setAttribute("stations", stations);
////            request.setAttribute("action", "edit");
////
////            request.getRequestDispatcher("/route.jsp").forward(request, response);
//
//
////            if (request.getParameter("json") != null) {
//            GsonBuilder builder = new GsonBuilder();
//            Gson gson = builder.create();
//
////                String json = new GsonBuilder ().toJson(someObject);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//
//            logger.info("doPostRoutesGet toJson -- " + gson.toJson(route));
//            response.getWriter().write(gson.toJson(route));
//
////            } else {
////                request.setAttribute("userRoute", userRoute);
////                request.getRequestDispatcher("/parts/orders.jsp").forward(request, response);
////            }
//
//
//        } catch (DBException e) {
//            logger.info("doGet edit SQLException -- " + e);
//
//            response.sendRedirect("/routes");
//        }
//    }

//    protected void doGetRoutesEdit(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        logger.info("doGetRoutesEdit -- " + request.getRequestURI());
//        logger.info("doGetRoutesEdit split [3] -- " + request.getRequestURI().split("/")[3]);
//
//        int id = Integer.parseInt(request.getRequestURI().split("/")[3]);
//
////        StationDAO stationDAO = new StationDAOImpl();
////        RouteDAO routeDAO = new RouteDAOImpl();
////        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();
//
//        try {
//            ArrayList<Station> stations = StationDAOImpl.getInstance().getAll();
//            Route route = RouteDAOImpl.getInstance().get(id);
//
//            ArrayList<Order> orders = OrderDAOImpl.getInstance().getAll();
//
//            ArrayList<FilterQuery> filters = FilterQuery.getList();
//            FilterQuery.addFilter(filters, "route_id", route.getId());
////            ArrayList<Order> ordersByRoute = OrderDAOImpl.getInstance().getFiltered1(filters);
//
//            filters = FilterQuery.getList();
//            FilterQuery.addFilter(filters, "user_id", request.getSession().getAttribute("userid"));
//            FilterQuery.addFilter(filters, "route_id", route.getId());
////            ArrayList<Order> ordersByCurrentUser = OrderDAOImpl.getInstance().getFiltered1(filters);
//
//            request.setAttribute("route", route);
//            request.setAttribute("stations", stations);
//            request.setAttribute("orders", orders);
////            request.setAttribute("ordersByRoute", ordersByRoute);
////            request.setAttribute("ordersByCurrentUser", ordersByCurrentUser);
//            request.setAttribute("action", "edit");
//
////            logger.info("doGetRoutesEdit userRoutes -- " + userRoutes);
//
//            request.getRequestDispatcher("/route.jsp").forward(request, response);
//        } catch (DBException e) {
//            logger.info("doGetRoutesEdit SQLException -- " + e);
//
//            response.sendRedirect("/routes");
//        }
//    }

//    protected void doGetRoutesAdd(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        logger.info("doGet add -- " + request.getRequestURI());
//
//        ArrayList<Station> stations = new ArrayList<>();
//
//        try {
//            stations = StationDAOImpl.getInstance().getAll();
////                request.setAttribute("stations", stations);
////                request.setAttribute("action", "add");
//
////                request.getRequestDispatcher("/route.jsp").forward(request, response);
//        } catch (DBException e) {
//            logger.info("doGet edit SQLException -- " + e);
//
//            response.sendRedirect("/routes");
//        }
//        request.setAttribute("stations", stations);
//        request.setAttribute("action", "add");
//
//        request.getRequestDispatcher("/route.jsp").forward(request, response);
//    }


//    protected void doPostRoutesEdit(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        logger.info("doPost contains edit -- " + request.getRequestURI());
//        logger.info("doPost contains edit split [3] -- " + request.getRequestURI().split("/")[3]);
//        logger.info("doPost station_departure -- " + request.getParameter("station_departure"));
//        logger.info("doPost station_arrival -- " + request.getParameter("station_arrival"));
//
//        int id = Integer.parseInt(request.getRequestURI().split("/")[3]);
//
////        RouteDAO routeDAO = new RouteDAOImpl();
//        try {
//            String train_number = String.valueOf(request.getParameter("train_number"));
//            Integer station_departure_id = Integer.valueOf(request.getParameter("station_departure"));
//            Integer station_arrival_id = Integer.valueOf(request.getParameter("station_arrival"));
//            String date_departure = String.valueOf(request.getParameter("date_departure"));
//            String date_arrival = String.valueOf(request.getParameter("date_arrival"));
////                int travel_time = Integer.valueOf(request.getParameter("travel_time"));
//            int travel_cost = Integer.valueOf(request.getParameter("travel_cost"));
//            int seats_available = Integer.valueOf(request.getParameter("seats_available"));
//            int seats_total = Integer.valueOf(request.getParameter("seats_total"));
//
//
//            Route route = new Route(0,
//                    train_number,
//                    new Station.Builder().withId(station_departure_id).build(),
//                    new Station.Builder().withId(station_arrival_id).build(),
//                    date_departure,
//                    date_arrival,
//                    travel_cost,
//                    seats_available,
//                    seats_total);
//            RouteDAOImpl.getInstance().update(id, route);
//            request.getSession().setAttribute("messages", "Route updated!");
//            request.getSession().setAttribute("url", "/routes");
//
//            response.sendRedirect("/routes/edit/" + id);
//        } catch (DBException e) {
//            String errorString = e.toString();
//            logger.info("doPost edit SQLIntegrityConstraintViolationException -- " + e);
//            request.getSession().setAttribute("errors", errorString);
//            response.sendRedirect("/routes");
//        }
//    }

//    protected void doPostRoutesAdd(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        logger.info("doPost contains add -- " + request.getRequestURI());
//
//        logger.info("doPost getParameter.travel_cost: {}", request.getParameter("travel_cost"));
//
//        try {
//            String train_number = String.valueOf(request.getParameter("train_number"));
//            Integer station_departure_id = Integer.valueOf(request.getParameter("station_departure"));
//            Integer station_arrival_id = Integer.valueOf(request.getParameter("station_arrival"));
//            String date_departure = String.valueOf(request.getParameter("date_departure"));
//            String date_arrival = String.valueOf(request.getParameter("date_arrival"));
////                int travel_time = Integer.valueOf(request.getParameter("travel_time"));
//            int travel_cost = Integer.valueOf(request.getParameter("travel_cost"));
////            int seats_available = Integer.valueOf(request.getParameter("seats_available"));
//            int seats_total = Integer.valueOf(request.getParameter("seats_total"));
//
//
//            Route route = new Route(
//                    0,
//                    train_number,
//                    new Station.Builder().withId(station_departure_id).build(),
//                    new Station.Builder().withId(station_arrival_id).build(),
//                    date_departure,
//                    date_arrival,
//                    travel_cost,
//                    seats_total,
//                    seats_total
//            );
//            RouteDAOImpl.getInstance().add(route);
//
//            request.getSession().setAttribute("messages", "Route " + train_number + " added!");
//
//        } catch (DBException e) {
//        }
//
//        response.sendRedirect("/routes");
//    }

//    protected void doPostRoutesDelete(HttpServletRequest request, HttpServletResponse response) throws
//            ServletException, IOException {
//
//        // TODO: getPathInfo=/7 -- переделать на getPathInfo получение айди
//
//        logger.info("doPost contains delete -- " + request.getRequestURI());
//        logger.info("doPost contains delete split [3] -- " + request.getRequestURI().split("/")[3]);
//
//        int id = Integer.parseInt(request.getRequestURI().split("/")[3]);
//
//        request.getSession().setAttribute("url", "/profile");
//
////        RouteDAO routeDAO = new RouteDAOImpl();
//        try {
//            RouteDAOImpl.getInstance().delete(id);
//            request.getSession().setAttribute("messages", "Route deleted!");
//            request.setAttribute("action", "delete");
//
////                request.getRequestDispatcher("/route.jsp").forward(request, response);
//        } catch (DBException e) {
//            String errorString = e.toString();
//            logger.info("doPost delete SQLIntegrityConstraintViolationException -- " + e);
////            if (e.getErrorCode() == 1451) {
////                String nameDatabase = errorString.split("CONSTRAINT")[0].split("`")[3];
////                logger.info("doPost getErrorCode -- 1451 -- DB: " + nameDatabase);
////                request.getSession().setAttribute(
////                        "errors",
////                        "Cannot delete item with id " + id + "! It was found in table: " + nameDatabase);
////            } else {
//            request.getSession().setAttribute("errors", errorString);
////            }
//            response.sendRedirect("/routes");
//        }
//
//        response.sendRedirect("/routes");
//    }
}
