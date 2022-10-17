package com.itermit.railway.controller;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.DBException;
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


@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SearchServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.debug("#doGet(request, response).  {}", request.getRequestURI());

        String commandName;
        if (request.getRequestURI().equals("/search")) {

            commandName = "searchGet";

        } else {
            request.setAttribute("error", "UNHANDLED request: " + request.getRequestURI());
            request.getRequestDispatcher("/error").forward(request, response);

            logger.error("doGet UNHANDLED request!  {}", request.getRequestURI());
            return;
        }

        CommandContainer.runCommand(request, response, commandName);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.debug("#doPost(request, response).  {}", request.getRequestURI());


        String commandName = null;

        if (request.getRequestURI().equals("/search")) {

            String action = request.getParameter("action");

            logger.info("doPost -> getParameter action: " + action);

            if (action != null && !action.isEmpty() && action.equals("reset")) {
                if (action.equals("reset")) {

                    commandName = "searchReset";

//                } else if (action.equals("search")) {
//
//                    /* [POST] SEARCH -- SEARCH JSON */
//                    doPostSearchJson(request, response);
                } else {
                    logger.info("doPost !!! FIXME !!! UNHANDLED action -- " + action);
                }
            } else {

                commandName = "searchPost";
            }

        } else if (request.getRequestURI().startsWith("/search/reserve/")) {
            /* [POST] SEARCH -- RESERVE */
            doPostReserve(request, response);
        } else {
            logger.info("doPost !!! FIXME !!! UNHANDLED getRequestURI() -- " + request.getRequestURI());
        }

        CommandContainer.runCommand(request, response, commandName);

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


        try {
            int id = CommandContainer.getIdFromRequest(request);
            Route route = RouteDAOImpl.getInstance().get(id);

//            ArrayList<Order> userRoutes = orderDAO.getAll();

            ArrayList<FilterQuery> filters = FilterQuery.getList();
            FilterQuery.addFilter(filters, "route_id", route.getId());
            ArrayList<Order> userRoutesByRoute = OrderDAOImpl.getInstance().getFiltered(filters);

//            filters = FilterQuery.getList();
//            FilterQuery.addFilter(filters, "user_id", userid);
//            FilterQuery.addFilter(filters, "route_id", route.getId());
//            ArrayList<Order> userRoutesByCurrentUser = OrderDAOImpl.getInstance().getFiltered(filters);

            request.setAttribute("route", route);
//            request.setAttribute("userRoutes", userRoutes);
            request.setAttribute("userRoutesByRoute", userRoutesByRoute);
//            request.setAttribute("userRoutesByCurrentUser", userRoutesByCurrentUser);
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

//        RouteDAO routeDAO = new RouteDAOImpl();

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


    protected void doPostReserve(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        int seats_reserve = Integer.parseInt(request.getParameter("seats_reserve"));
//        int id = CommandContainer.getIdFromRequest(request);

        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        if (userid == null) {
//            logger.info("doPostSearchReserve -- Empty ID!");
//            request.getSession().setAttribute("errors", "Empty ID!");
//            request.getRequestDispatcher("/search.jsp").forward(request, response);
            return;
        }

        logger.info("doPostSearchReserve seats_reserve -- " + seats_reserve);

//        OrderDAO userRouteDAO = new OrderDAOImpl();
//        try {
//            Order userRoute = new Order(
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
