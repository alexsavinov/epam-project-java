package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.OrderDAO;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderDAOImpl implements OrderDAO {

    private DBManager dbManager;
    private static OrderDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);

    private static final String SQL_GET_ALL_ORDERS = "SELECT " +
            "orders.id, " +
            "orders.seats, " +
            "orders.user_id, " +
            "orders.route_id, " +
            "orders.date_reserve, " +
            "users.name as user_name, " +
            "routes.train_number as route_train_number, " +
            "routes.station_departure_id as station_departure_id, " +
            "routes.station_arrival_id as station_arrival_id, " +
            "routes.travel_cost as route_travel_cost, " +
            "routes.seats_reserved as route_seats_reserved, " +
            "routes.seats_total as route_seats_total, " +
            "routes.date_departure as route_date_departure, " +
            "routes.date_arrival as route_date_arrival, " +
            "s_d.name as station_departure_name, " +
            "s_a.name as station_arrival_name " +
            "FROM orders " +
            "LEFT JOIN users ON orders.user_id = users.id " +
            "LEFT JOIN routes ON orders.route_id = routes.id " +
            "LEFT JOIN stations s_a ON routes.station_arrival_id = s_a.id " +
            "LEFT JOIN stations s_d ON routes.station_departure_id = s_d.id";
    public static final String SQL_GET_ORDER_BY_ID = "SELECT " +
            "orders.id, " +
            "orders.seats, " +
            "orders.user_id, " +
            "orders.route_id, " +
            "orders.date_reserve, " +
            "users.name as user_name, " +
            "routes.train_number as route_train_number, " +
            "routes.station_departure_id as station_departure_id, " +
            "routes.station_arrival_id as station_arrival_id, " +
            "routes.travel_cost as route_travel_cost, " +
            "routes.seats_total as route_seats_total, " +
            "routes.seats_reserved as route_seats_reserved, " +
            "routes.seats_total - routes.seats_reserved as route_seats_available, " +
            "routes.date_departure as route_date_departure, " +
            "routes.date_arrival as route_date_arrival, " +
            "s_d.name as station_departure_name, " +
            "s_a.name as station_arrival_name " +
            "FROM orders " +
            "LEFT JOIN users ON orders.user_id = users.id " +
            "LEFT JOIN routes ON orders.route_id = routes.id " +
            "LEFT JOIN stations s_a ON routes.station_arrival_id = s_a.id " +
            "LEFT JOIN stations s_d ON routes.station_departure_id = s_d.id " +
            "WHERE orders.id = ?";

    private static final String SQL_ADD_ORDER = "INSERT " +
            "INTO orders (user_id, route_id, seats, date_reserve) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_ORDER = "UPDATE orders " +
            "SET user_id = ?, route_id = ?, seats = ?, date_reserve = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
    private static final String SQL_GET_GROUPED_BY_ROUTE = "" +
            "SELECT route_id, " +
            "       r.id, " +
            "       r.train_number, " +
            "       r.travel_cost, " +
            "       s_d.name as station_departure_name, " +
            "       s_a.name as station_arrival_name, " +
            "       sum(seats) as seats " +
            "FROM orders " +
            "         LEFT JOIN routes r on r.id = orders.route_id " +
            "         LEFT JOIN stations s_d on r.station_departure_id = s_d.id " +
            "         LEFT JOIN stations s_a on r.station_arrival_id = s_a.id " +
            "WHERE user_id = ? " +
            "GROUP BY orders.route_id";
    private static final String SQL_ADD_RESERVE_ORDER = "UPDATE orders " +
            "SET seats = seats + ? " +
            "WHERE id = ?";
    public static final String SQL_REMOVE_RESERVE_ORDER = "UPDATE orders SET seats = seats - ? WHERE id = ?";
    public static final String SQL_CHECK_RESERVE_ORDER = "SELECT id FROM orders WHERE id = ? AND seats < 0";

    public static synchronized OrderDAOImpl getInstance() {
        if (instance == null) {
            instance = new OrderDAOImpl();
        }
        return instance;
    }

    private OrderDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException {

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ORDERS);
//            logger.trace(SQL_GET_ALL_ORDERS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(extract(resultSet));
            }
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeResultSet(resultSet);
        }

        return orders;
    }

    public Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException {

        ArrayList<Order> orders = getFiltered(connection, query);

        int totalPages = (int) Math.ceil(orders.size() / (float) (Paginator.PAGE_SIZE));

        return new Paginator.Builder()
                .withPages(totalPages)
                .withData(orders)
                .build();
    }

    public ArrayList<Order> getFiltered(Connection connection, QueryMaker query) throws SQLException {
        logger.debug("#getFiltered(query).");

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            query.setQueryMain(SQL_GET_ALL_ORDERS);
            preparedStatement = query.getPreparedStatement(connection);
//            logger.trace(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(extract(resultSet));
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return orders;
    }

    public ArrayList<Order> getGroupedByRouteOfUser(Connection connection, int userId)
            throws SQLException {

        logger.debug("#getGroupedByRouteOfUser(connection, userId). {}", userId);

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_GROUPED_BY_ROUTE);
            int l = 0;
            preparedStatement.setInt(++l, userId);
            logger.trace(preparedStatement);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                private static final String SQL_GET_GROUPED_BY_ROUTE = "" +
//                        "SELECT route_id, " +
//                        "       r.id, " +
//                        "       r.train_number, " +
//                        "       s_d.name as station_departure_name, " +
//                        "       s_a.name as station_arrival_name, " +
//                        "       sum(seats) as seats " +
//                        "FROM orders " +
//                        "         LEFT JOIN routes r on r.id = orders.route_id " +
//                        "         LEFT JOIN stations s_d on r.station_departure_id = s_d.id " +
//                        "         LEFT JOIN stations s_a on r.station_arrival_id = s_a.id " +
//                        "WHERE user_id = ? " +
//                        "GROUP BY orders.route_id";
                Station stationDeparture = new Station.Builder()
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station stationArrival = new Station.Builder()
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                Route route = new Route.Builder()
                        .withId(resultSet.getInt("route_id"))
                        .withTrainNumber(resultSet.getString("train_number"))
                        .withTravelCost(resultSet.getInt("travel_cost"))
                        .withStationDeparture(stationDeparture)
                        .withStationArrival(stationArrival)
                        .build();

                orders.add(new Order.Builder()
                        .withRoute(route)
                        .withSeats(resultSet.getInt("seats"))
                        .build());
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return orders;
    }

    @Override
    public Order get(Connection connection, int id) throws SQLException {

        logger.debug("#get(id): {}", id);

        Order order = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ORDER_BY_ID);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                order = extract(resultSet);
            }

        } catch (SQLException e) {
            logger.error("Error while get(id): {}", e.getMessage());
//            throw new DBException("Error while get(id)!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return order;
    }

    @Override
    public void add(Connection connection, Order order) throws SQLException {

        logger.debug("#add(order): {}", order);

        PreparedStatement preparedStatement = null;

        try {
            /* Adding new Order */
            preparedStatement = connection.prepareStatement(SQL_ADD_ORDER);
            int l = 0;
            preparedStatement.setInt(++l, order.getUser().getId());
            preparedStatement.setInt(++l, order.getRoute().getId());
            preparedStatement.setInt(++l, order.getSeats());
            preparedStatement.setString(++l, order.getDateReserve());
            logger.trace(preparedStatement);
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void update(Connection connection, int id, Order order) throws SQLException {

        logger.debug("#update(id, order): {} -- {}", id, order);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDER);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            System.out.println(formatter.format(new Date(System.currentTimeMillis())));

            int l = 0;
            preparedStatement.setInt(++l, order.getUser().getId());
            preparedStatement.setInt(++l, order.getRoute().getId());
            preparedStatement.setInt(++l, order.getSeats());
            preparedStatement.setString(++l, order.getDateReserve());
//            preparedStatement.setString(++l, formatter.format(new Date(System.currentTimeMillis())));
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while update(id, order): {}", e.getMessage());
//            throw new DBException("Error while update(id, order)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void delete(Connection connection, int id) throws SQLException {

        logger.debug("#delete(id): {}", id);

//        ResultSet resultSet;
        PreparedStatement preparedStatement = null;
//        Connection connection = null;
        try {
//            /* Set transaction isolation */
//            connection = dbManager.getConnection();
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            connection.setAutoCommit(false);
//
//            preparedStatement = connection.prepareStatement(SQL_GET_ORDER_BY_ID);
//            preparedStatement.setInt(1, id);
//            logger.trace(preparedStatement);
//            resultSet = preparedStatement.executeQuery();

//            if (resultSet.next()) {
//                int route_id = resultSet.getInt("route_id");
//                int routeSeats = resultSet.getInt("seats");
//                int seatsToRemove = routeSeats;
//                if (this.seats != 0) {
//                    seatsToRemove = this.seats;
//                }
//
//                /* Remove reserved Seats from Route */
//                preparedStatement = connection.prepareStatement(SQL_REMOVE_RESERVE_ROUTE);
//                int l = 0;
//                preparedStatement.setInt(++l, seatsToRemove);
//                preparedStatement.setInt(++l, route_id);
//                logger.trace(preparedStatement);
//                preparedStatement.executeUpdate();
//
//                /* Check if reserved Seats in Route remains above zero */
//                preparedStatement = connection.prepareStatement(SQL_CHECK_RESERVE_ROUTE);
//                preparedStatement.setInt(1, route_id);
//                logger.trace(preparedStatement);
//                ResultSet resultSetRoute = preparedStatement.executeQuery();
//                if (resultSetRoute.next()) {
//                    /* Rollback */
//                    connection.rollback();
//                    logger.error("Not enough reserve to remove from Route (id): {}", route_id);
////                    throw new DBException("Not enough reserve to remove from Route (id): " + route_id, null);
//                }
//
//                logger.info("this.seats {}", this.seats);
//                logger.info("seatsToRemove {}", seatsToRemove);

//                if (seatsToRemove == routeSeats) {
            /* Seats to remove equals to Total seats in Order - deleting Order */
            preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
//                } else {
//                    /* Seats to remove less than Total seats in Order - decreasing Seats in Order */
//                    preparedStatement = connection.prepareStatement(SQL_REMOVE_RESERVE_ORDER);
//                    l = 0;
//                    preparedStatement.setInt(++l, seatsToRemove);
//                    preparedStatement.setInt(++l, id);
//                    preparedStatement.executeUpdate();
//
//                    /* Check if Seats in Order remains above zero */
//                    preparedStatement = connection.prepareStatement(SQL_CHECK_RESERVE_ORDER);
//                    preparedStatement.setInt(1, id);
//                    ResultSet resultSetOrder = preparedStatement.executeQuery();
//                    if (resultSetOrder.next()) {
//                        /* Rollback */
//                        connection.rollback();
//                        logger.error("Not enough reserved Seats to remove from Order (id): {}", id);
////                        throw new DBException("Not enough reserved Seats to remove from Order (id): " + id, null);
//                    }
//                }
//            }
//            /* Commit */
//            connection.commit();

//        } catch (SQLException e) {
//            try {
//                /* Rollback */
//                connection.rollback();
//            } catch (SQLException ex) {
//                logger.error("Error while rollback - delete(id): {}", e.getMessage());
////                throw new DBException("Error while rollback - delete(id)!", e);
//            }
//            logger.error("Error while delete(id): {}", e.getMessage());
////            throw new DBException("Error while delete(id)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

    @Override
    public Order extract(ResultSet resultSet) throws SQLException {

        User user = new User.Builder()
                .withId(resultSet.getInt("user_id"))
                .withName(resultSet.getString("user_name"))
                .build();

        Station stationDeparture = new Station.Builder()
                .withId(resultSet.getInt("station_departure_id"))
                .withName(resultSet.getString("station_departure_name"))
                .build();

        Station stationArrival = new Station.Builder()
                .withId(resultSet.getInt("station_arrival_id"))
                .withName(resultSet.getString("station_arrival_name"))
                .build();

        Route route = new Route.Builder()
                .withId(resultSet.getInt("route_id"))
                .withTrainNumber(resultSet.getString("route_train_number"))
                .withStationDeparture(stationDeparture)
                .withStationArrival(stationArrival)
                .withDateDeparture(resultSet.getString("route_date_departure"))
                .withDateArrival(resultSet.getString("route_date_arrival"))
                .withTravelCost(resultSet.getInt("route_travel_cost"))
                .withSeatsReserved(resultSet.getInt("route_seats_reserved"))
                .withSeatsTotal(resultSet.getInt("route_seats_total"))
                .build();

        return new Order.Builder()
                .withId(resultSet.getInt("id"))
                .withUser(user)
                .withRoute(route)
                .withSeats(resultSet.getInt("seats"))
                .withDateReserve(resultSet.getString("date_reserve"))
                .build();
    }

}
