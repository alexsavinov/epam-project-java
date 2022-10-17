package com.itermit.railway.controller;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "OrderServlet",
        urlPatterns = {"/orders", "/orders/delete/*", "/orders/edit/*", "/orders/add"})
public class OrderServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(OrderServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("doGet getPathInfo=" + request.getPathInfo());
        logger.info("doGet getRequestURI=" + request.getRequestURI());

        Boolean isAuthorized = false;
        if (request.getSession().getAttribute("isAuthorized") != null) {
            isAuthorized = (Boolean) request.getSession().getAttribute("isAuthorized");
        }

        logger.info("doGet isAuthorized = " + isAuthorized);
        if (!isAuthorized) {
            logger.info("doGet is NOT Authorized -> auth.jsp");
            request.getRequestDispatcher("/auth.jsp").forward(request, response);
        }

        if (request.getRequestURI().equals("/orders")) {
            /* [GET] ROUTES -- LIST */
            doGetUsersRoutes(request, response);

        } else if (request.getRequestURI().startsWith("/orders/delete")) {
            /* [GET] ROUTES -- DELETE */
            logger.info("doGet contains delete -- forward to ->  " + request.getRequestURI());
            doPost(request, response);

        } else if (request.getRequestURI().startsWith("/orders/edit")) {
            /* [GET] ROUTES -- EDIT */
            doGetUserRouteEdit(request, response);

        } else if (request.getRequestURI().startsWith("/orders/add")) {
            /* [GET] ROUTES -- ADD */
            doGetUserRouteAdd(request, response);

        } else {
            logger.info("doGet UNHANDLED request.getRequestURI() -- " + request.getRequestURI());
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

        if (isAuthorized.equals(false)) {
            /* [POST] NOT AUTHORISED */
            logger.info("doPost is NOT Authorized -> auth.jsp");
            request.getRequestDispatcher("/auth.jsp").forward(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/orders/edit")) {
            /* [POST] ROUTES -- EDIT */
            doPostUserRouteEdit(request, response);

        } else if (request.getRequestURI().startsWith("/orders/delete")) {
            /* [POST] ROUTES -- DELETE */
            doPostUserRouteDelete(request, response);

        } else if (request.getRequestURI().equals("/orders/add")) {
            /* [POST] ROUTES -- ADD */
            doPostUserRouteAdd(request, response);

        } else {
            logger.info("doPost !!! FIXME !!! UNHANDLED getRequestURI() -- " + request.getRequestURI());
        }
    }


    protected void doGetUsersRoutes(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doGet isAuthorized -> routes.jsp");

        ArrayList<Order> orders;
//        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();
        try {
            orders = OrderDAOImpl.getInstance().getAll();
        } catch (DBException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("orders", orders);

        request.getRequestDispatcher("orders.jsp").forward(request, response);
    }

    protected void doGetUserRouteEdit(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doGetUserRouteEdit contains edit -- " + request.getRequestURI());
        logger.info("doGetUserRouteEdit contains edit split [3] -- " + request.getRequestURI().split("/")[3]);

        int id = Integer.parseInt(request.getRequestURI().split("/")[3]);

//        ArrayList<User> users = new ArrayList<>();
//        ArrayList<Route> routes = new ArrayList<>();

        try {
            ArrayList<User> users = UserDAOImpl.getInstance().getAll();
            ArrayList<Route> routes = RouteDAOImpl.getInstance().getAll();
            Order order = OrderDAOImpl.getInstance().get(id);

            logger.info("order -- " + order);

            request.setAttribute("users", users);
            request.setAttribute("routes", routes);
            request.setAttribute("order", order);
            request.setAttribute("action", "edit");

            request.getRequestDispatcher("/order.jsp").forward(request, response);
        } catch (DBException e) {
            logger.info("doGetUserRouteEdit SQLException -- " + e);

            response.sendRedirect("/orders");
        }
    }

    protected void doGetUserRouteAdd(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doGetUserRouteAdd add -- " + request.getRequestURI());

        ArrayList<User> users;
        ArrayList<Route> routes;

//        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();

        try {
            users = UserDAOImpl.getInstance().getAll();
            routes = RouteDAOImpl.getInstance().getAll();

            request.setAttribute("users", users);
            request.setAttribute("routes", routes);
            request.setAttribute("action", "add");
        } catch (DBException e) {
            logger.info("doGetUserRouteAdd SQLException -- " + e);

            response.sendRedirect("/orders");
        }

        request.getRequestDispatcher("/order.jsp").forward(request, response);
    }


    protected void doPostUserRouteEdit(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doPostUserRouteEdit-- " + request.getRequestURI());
        logger.info("doPostUserRouteEdit split [3] -- " + request.getRequestURI().split("/")[3]);
        try {

            int id = Integer.parseInt(request.getRequestURI().split("/")[3]);

//        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();
            User user = new User.Builder()
                    .withId(Integer.parseInt(request.getParameter("user"))).build();

            int routeId =  Integer.parseInt(request.getParameter("route"));

            Route route = new Route.Builder().withId(routeId).build();
//            Route route = new Route(
//                    Integer.parseInt(request.getParameter("route")),
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    0,
//                    0,
//                    0
//            );

            Order order = new Order(
                    0,
                    user,
                    route,
                    CommandContainer.getIntegerFromRequest(request, "seats"),
                    CommandContainer.getStringFromRequest(request, "date_reserve")
            );
            logger.info("order -- " + order);

            OrderDAOImpl.getInstance().update(id, order);

            request.getSession().setAttribute("messages", "Order updated!");
            request.getSession().setAttribute("url", "/orders");

            response.sendRedirect("/orders/edit/" + id);
        } catch (DBException e) {
            String errorString = e.toString();
            logger.info("doPostUserRouteEdit SQLIntegrityConstraintViolationException -- " + e);
            request.getSession().setAttribute("errors", errorString);
            response.sendRedirect("/orders");
        }
    }

    protected void doPostUserRouteAdd(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        logger.info("doPostUserRouteAdd contains add -- " + request.getRequestURI());

//        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();
        try {
            User user = new User.Builder()
                    .withId(Integer.parseInt(request.getParameter("user"))).build();

            int routeId =  Integer.parseInt(request.getParameter("route"));

            Route route = new Route.Builder().withId(routeId).build();
//                    Integer.parseInt(request.getParameter("route")),
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    0,
//                    0,
//                    0
//            );

            Order order = new Order(
                    0,
                    user,
                    route,
                    CommandContainer.getIntegerFromRequest(request, "seats"),
                    CommandContainer.getStringFromRequest(request, "date_reserve")
            );

            logger.info("order -- " + order);

            OrderDAOImpl.getInstance().add(order);

            request.getSession().setAttribute("messages", "Order added!");

        } catch (DBException e) {
            logger.info("doPost add SQLException -- !!! FIXME !!! UNHANDLED EXCEPTION -- " + e);
            request.getSession().setAttribute("errors", "!!! FIXME !!! UNHANDLED EXCEPTION: " + e);
        }

        response.sendRedirect("/orders");
    }

    protected void doPostUserRouteDelete(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {


//        UserRouteDAO userRouteDAO = new UserRouteDAOImpl();
        try {
        int id = CommandContainer.getIdFromRequest(request);

            request.getSession().setAttribute("url", "/profile");
            OrderDAOImpl.getInstance().delete(id);
            request.getSession().setAttribute("messages", "Order deleted!");
            request.setAttribute("action", "delete");

//                request.getRequestDispatcher("/route.jsp").forward(request, response);
        } catch (DBException e) {
            String errorString = e.toString();
            logger.info("doPostUserRouteDelete SQLIntegrityConstraintViolationException -- " + e);
//            if (e.getErrorCode() == 1451) {
//                String nameDatabase = errorString.split("CONSTRAINT")[0].split("`")[3];
//                logger.info("doPost getErrorCode -- 1451 -- DB: " + nameDatabase);
//                request.getSession().setAttribute(
//                        "errors",
//                        "Cannot delete item with id " + id + "! It was found in table: " + nameDatabase);
//            } else {
//                request.getSession().setAttribute("errors", errorString);
//            }
//                response.sendRedirect("/routes");
        }

        response.sendRedirect("/orders");
    }
}
