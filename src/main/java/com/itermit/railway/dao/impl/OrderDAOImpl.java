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

import static com.itermit.railway.db.Fields.*;

/**
 * DAO Implementation for Orders.
 * <p>
 * Processes request to database from OrderManager with a given connection.
 *
 * @author O.Savinov
 */
public class OrderDAOImpl implements OrderDAO {

    private DBManager dbManager;
    private static OrderDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);
    /* SQL Queries */
    private static final String SQL_GET_ALL_ORDERS = "" +
            "SELECT " +
            "   orders.id, " +
            "   orders.seats, " +
            "   orders.user_id, " +
            "   orders.route_id, " +
            "   orders.date_reserve, " +
            "   users.name as user_name, " +
            "   routes.train_number as route_train_number, " +
            "   routes.station_departure_id as station_departure_id, " +
            "   routes.station_arrival_id as station_arrival_id, " +
            "   routes.travel_cost as route_travel_cost, " +
            "   routes.seats_reserved as route_seats_reserved, " +
            "   routes.seats_total as route_seats_total, " +
            "   routes.date_departure as route_date_departure, " +
            "   routes.date_arrival as route_date_arrival, " +
            "   s_d.name as station_departure_name, " +
            "   s_a.name as station_arrival_name " +
            "FROM orders " +
            "   LEFT JOIN users ON orders.user_id = users.id " +
            "   LEFT JOIN routes ON orders.route_id = routes.id " +
            "   LEFT JOIN stations s_a ON routes.station_arrival_id = s_a.id " +
            "   LEFT JOIN stations s_d ON routes.station_departure_id = s_d.id";
    public static final String SQL_GET_ORDER_BY_ID = "" +
            "SELECT " +
            "   orders.id, " +
            "   orders.seats, " +
            "   orders.user_id, " +
            "   orders.route_id, " +
            "   orders.date_reserve, " +
            "   users.name as user_name, " +
            "   routes.train_number as route_train_number, " +
            "   routes.station_departure_id as station_departure_id, " +
            "   routes.station_arrival_id as station_arrival_id, " +
            "   routes.travel_cost as route_travel_cost, " +
            "   routes.seats_total as route_seats_total, " +
            "   routes.seats_reserved as route_seats_reserved, " +
            "   routes.seats_total - routes.seats_reserved as route_seats_available, " +
            "   routes.date_departure as route_date_departure, " +
            "   routes.date_arrival as route_date_arrival, " +
            "   s_d.name as station_departure_name, " +
            "   s_a.name as station_arrival_name " +
            "FROM orders " +
            "   LEFT JOIN users ON orders.user_id = users.id " +
            "   LEFT JOIN routes ON orders.route_id = routes.id " +
            "   LEFT JOIN stations s_a ON routes.station_arrival_id = s_a.id " +
            "   LEFT JOIN stations s_d ON routes.station_departure_id = s_d.id " +
            "WHERE orders.id = ?";
    private static final String SQL_ADD_ORDER = "" +
            "INSERT " +
            "INTO orders (user_id, route_id, seats, date_reserve) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_ORDER = "" +
            "UPDATE orders " +
            "SET user_id = ?, route_id = ?, seats = ?, date_reserve = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_ORDER = "" +
            "DELETE FROM orders " +
            "WHERE id = ?";
    private static final String SQL_GET_GROUPED_BY_ROUTE = "" +
            "SELECT route_id, " +
            "   r.id, " +
            "   r.train_number, " +
            "   r.travel_cost, " +
            "   s_d.name as station_departure_name, " +
            "   s_a.name as station_arrival_name, " +
            "   sum(seats) as seats " +
            "FROM orders " +
            "   LEFT JOIN routes r on r.id = orders.route_id " +
            "   LEFT JOIN stations s_d on r.station_departure_id = s_d.id " +
            "   LEFT JOIN stations s_a on r.station_arrival_id = s_a.id " +
            "WHERE user_id = ? " +
            "GROUP BY orders.route_id";
    private static final String SQL_ADD_RESERVE_ORDER = "" +
            "UPDATE orders " +
            "SET seats = seats + ? " +
            "WHERE id = ?";
    public static final String SQL_REMOVE_RESERVE_ORDER = "" +
            "UPDATE orders " +
            "SET seats = seats - ? " +
            "WHERE id = ?";
    public static final String SQL_CHECK_RESERVE_ORDER = "" +
            "SELECT id " +
            "FROM orders " +
            "WHERE id = ? AND seats < 0";

    /**
     * Default constructor
     */
    private OrderDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    /**
     * Returns an instance of Order's DAO implementation.
     *
     * @return OrderDAOImpl
     */
    public static synchronized OrderDAOImpl getInstance() {
        if (instance == null) {
            instance = new OrderDAOImpl();
        }
        return instance;
    }

    /**
     * Returns a list of Orders.
     *
     * @param connection Connection to DataSource
     * @return ArrayList of Orders
     * @throws SQLException
     */
    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException {

        logger.debug("#getAll(connection).");

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ORDERS);
            logger.trace(SQL_GET_ALL_ORDERS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(extract(resultSet));
            }
        } finally {
            dbManager.closePreparedStatement(preparedStatement);
            dbManager.closeResultSet(resultSet);
        }

        return orders;
    }

    /**
     * Returns wrapped by Paginator list of Orders.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return Paginated list of Orders
     * @throws SQLException
     */
    public Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException {

        logger.debug("#getPaginated(connection, query).");

        ArrayList<Order> orders = getFiltered(connection, query);

        int totalPages = (int) Math.ceil(orders.size() / (float) (Paginator.PAGE_SIZE));

        return new Paginator.Builder()
                .withPages(totalPages)
                .withData(orders)
                .build();
    }

    /**
     * Returns a filtered list of Orders.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return ArrayList of Orders
     * @throws SQLException
     */
    public ArrayList<Order> getFiltered(Connection connection, QueryMaker query)
            throws SQLException {

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
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return orders;
    }

    /**
     * Returns a grouped by Route list of Orders for related User.
     *
     * @param connection Connection to DataSource
     * @param userId     Integer value of User id
     * @return ArrayList of Orders grouped by Route
     * @throws SQLException
     */
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
                Station stationDeparture = new Station.Builder()
                        .withName(resultSet.getString(ROUTE_STATION_DEPARTURE_NAME))
                        .build();

                Station stationArrival = new Station.Builder()
                        .withName(resultSet.getString(ROUTE_STATION_ARRIVAL_NAME))
                        .build();

                Route route = new Route.Builder()
                        .withId(resultSet.getInt(ORDER_ROUTE_ID))
                        .withTrainNumber(resultSet.getString(ROUTE_TRAIN_NUMBER))
                        .withTravelCost(resultSet.getInt(ROUTE_TRAVEL_COST))
                        .withStationDeparture(stationDeparture)
                        .withStationArrival(stationArrival)
                        .build();

                orders.add(new Order.Builder()
                        .withRoute(route)
                        .withUser(new User.Builder().withId(userId).build())
                        .withSeats(resultSet.getInt(ORDER_SEATS))
                        .build());
            }
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return orders;
    }

    /**
     * Returns an Order by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of Order id
     * @return Order
     * @throws SQLException
     */
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
        } finally {
            dbManager.closeResultSet(resultSet);
            dbManager.closePreparedStatement(preparedStatement);
        }

        return order;
    }

    /**
     * Adds a new Order.
     *
     * @param connection Connection to DataSource
     * @param order      Order to add
     * @throws SQLException
     */
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
            dbManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Updates existed Order.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of Order id
     * @param order      Order data to update
     * @throws SQLException
     */
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
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();
        } finally {
            dbManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Deletes existed Order by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of Order id
     * @throws SQLException
     */
    @Override
    public void delete(Connection connection, int id) throws SQLException {

        logger.debug("#delete(id): {}", id);

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } finally {
            dbManager.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * Extracts Order entity from given ResultSet.
     *
     * @param resultSet ResultSet to process
     * @return Order
     * @throws SQLException
     */
    @Override
    public Order extract(ResultSet resultSet) throws SQLException {

        User user = new User.Builder()
                .withId(resultSet.getInt(ORDER_USER_ID))
                .withName(resultSet.getString(ORDER_USER_NAME))
                .build();

        Station stationDeparture = new Station.Builder()
                .withId(resultSet.getInt(ROUTE_STATION_DEPARTURE_ID))
                .withName(resultSet.getString(ROUTE_STATION_DEPARTURE_NAME))
                .build();

        Station stationArrival = new Station.Builder()
                .withId(resultSet.getInt(ROUTE_STATION_ARRIVAL_ID))
                .withName(resultSet.getString(ROUTE_STATION_ARRIVAL_NAME))
                .build();

        Route route = new Route.Builder()
                .withId(resultSet.getInt(ORDER_ROUTE_ID))
                .withTrainNumber(resultSet.getString(ORDER_ROUTE_TRAIN_NUMBER))
                .withStationDeparture(stationDeparture)
                .withStationArrival(stationArrival)
                .withDateDeparture(resultSet.getString(ORDER_ROUTE_DATE_DEPARTURE))
                .withDateArrival(resultSet.getString(ORDER_ROUTE_DATE_ARRIVAL))
                .withTravelCost(resultSet.getInt(ORDER_ROUTE_TRAVEL_COST))
                .withSeatsReserved(resultSet.getInt(ORDER_ROUTE_SEATS_RESERVED))
                .withSeatsTotal(resultSet.getInt(ORDER_ROUTE_SEATS_TOTAL))
                .build();

        return new Order.Builder()
                .withId(resultSet.getInt(ENTITY_ID))
                .withUser(user)
                .withRoute(route)
                .withSeats(resultSet.getInt(ORDER_SEATS))
                .withDateReserve(resultSet.getString(ORDER_DATE_RESERVE))
                .build();
    }

}
