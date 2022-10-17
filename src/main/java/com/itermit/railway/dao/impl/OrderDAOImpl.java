package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.OrderDAO;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.FilterQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

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
    private static final String SQL_GET_ORDER_BY_ID = "SELECT " +
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
//            "LEFT JOIN orders ur ON routes.id = ur.route_id " +
            "WHERE orders.id = ?";
//            "GROUP BY routes.id ";

    private static final String SQL_ADD_ORDER = "INSERT " +
            "INTO orders (user_id, route_id, seats, date_reserve) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_ORDER = "UPDATE " +
            "orders SET user_id = ?, route_id = ?, seats = ?, date_reserve = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_ORDER = "DELETE FROM " +
            "orders WHERE id = ?";

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
    public ArrayList<Order> getAll() throws DBException {

        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ORDERS);

            logger.trace(SQL_GET_ALL_ORDERS);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
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
                        .witheatsTotal(resultSet.getInt("route_seats_total"))
                        .build();

                Order order = new Order(
                        resultSet.getInt("id"),
                        user,
                        route,
                        resultSet.getInt("seats"),
                        resultSet.getString("date_reserve"));

                orders.add(order);

                //            logger.info("! id: " + Route.getId() + "; name: " + Route.getName());
            }

        } catch (SQLException e) {
            logger.error("SQLException while getAll(): {}", e.getMessage());
            throw new DBException("SQLException while getAll()!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeResultSet(resultSet);
        }

        return orders;
    }

    public ArrayList<Order> getFiltered(ArrayList<FilterQuery> filters) throws DBException {

        logger.debug("#getFiltered()");

        logger.trace("filters: " + filters);

        ArrayList<Order> usersRoutes = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        StringBuilder sb = new StringBuilder(SQL_GET_ALL_ORDERS);
        for (int i = 0; i < filters.size(); i++) {
            FilterQuery filter = filters.get(i);

            if (i == 0)
                sb.append(" WHERE ");
            else
                sb.append(" AND ");

            if (filter.getField().equals("date_departure")
                    || filter.getField().equals("date_arrival")) {

                sb.append("routes.").append(filter.getField()).append(" ")
                        .append(filter.getCondition()).append(" '")
                        .append(filter.getValues().get(0)).append("'");
            } else {
                sb.append("orders.").append(filter.getField()).append(" IN (");

                sb.append(filter.getValues().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))).append(")");
            }

        }
        logger.info("getFiltered -- SQL_GET_ALL_USERSROUTES_FILTERED: " + sb);

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(sb.toString());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                User user = new User.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
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
                        .witheatsTotal(resultSet.getInt("route_seats_total"))
                        .build();

                Order userRoute = new Order(
                        resultSet.getInt("id"),
                        user,
                        route,
                        resultSet.getInt("seats"),
                        resultSet.getString("date_reserve"));

                usersRoutes.add(userRoute);

                //            logger.info("! id: " + Route.getId() + "; name: " + Route.getName());
            }

        } catch (SQLException e) {
            logger.error("SQLException while getFiltered(): {}", e.getMessage());
            throw new DBException("SQLException while getFiltered()!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return usersRoutes;
    }

    @Override
    public Order get(int id) throws DBException {

        logger.debug("#get(id): {}", id);

        Order order = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_GET_ORDER_BY_ID);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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
                        .witheatsTotal(resultSet.getInt("route_seats_total"))
                        .build();

                order = new Order(
                        resultSet.getInt("id"),
                        user,
                        route,
                        resultSet.getInt("seats"),
                        resultSet.getString("date_reserve")
                );
            }

        } catch (SQLException e) {
            logger.error("SQLException while get(id): {}", e.getMessage());
            throw new DBException("SQLException while get(id)!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return order;
    }

    @Override
    public void add(Order order) throws DBException {

        logger.debug("#add(order): {}", order);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_ADD_ORDER);
            int l = 0;
            preparedStatement.setInt(++l, order.getUser().getId());
            preparedStatement.setInt(++l, order.getRoute().getId());
            preparedStatement.setInt(++l, order.getSeats());
            preparedStatement.setString(++l, order.getDateReserve());

            logger.trace(preparedStatement);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while add(order): {}", e.getMessage());
            throw new DBException("SQLException while add(order)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void update(int id, Order order) throws DBException {

        logger.debug("#update(id, order): {} -- {}", id, order);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
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
            logger.error("SQLException while update(id, order): {}", e.getMessage());
            throw new DBException("SQLException while update(id, order)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void delete(int id) throws DBException {

        logger.debug("#delete(id): {}", id);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while delete(id): {}", e.getMessage());
            throw new DBException("SQLException while delete(id)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

}
