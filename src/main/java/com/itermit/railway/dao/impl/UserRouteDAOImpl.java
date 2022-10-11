package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.UserRouteDAO;
import com.itermit.railway.dao.entity.Route;
import com.itermit.railway.dao.entity.Station;
import com.itermit.railway.dao.entity.User;
import com.itermit.railway.dao.entity.UserRoute;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.FilterQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserRouteDAOImpl implements UserRouteDAO {

    private static final Logger logger = LogManager.getLogger(UserRouteDAOImpl.class);

    private static final String SQL_GET_ALL_USERSROUTES = "SELECT " +
            "users_routes.id, " +
            "users_routes.seats, " +
            "users_routes.user_id, " +
            "users_routes.route_id, " +
            "users_routes.date_reserve, " +
            "users.name as user_name, " +
            "routes.train_number as route_train_number, " +
            "routes.station_departure_id as station_departure_id, " +
            "routes.station_arrival_id as station_arrival_id, " +
            "routes.travel_cost as route_travel_cost, " +
            "0 as route_seats_available, " +
            "routes.seats_total as route_seats_total, " +
            "routes.date_departure as route_date_departure, " +
            "routes.date_arrival as route_date_arrival, " +
            "s_d.name as station_departure_name, " +
            "s_a.name as station_arrival_name " +
            "FROM users_routes " +
            "LEFT JOIN users ON users_routes.user_id = users.id " +
            "LEFT JOIN routes ON users_routes.route_id = routes.id " +
            "LEFT JOIN stations s_a ON routes.station_arrival_id = s_a.id " +
            "LEFT JOIN stations s_d ON routes.station_departure_id = s_d.id";
    private static final String SQL_GET_USERROUTE_BY_ID = "SELECT " +
            "users_routes.id, " +
            "users_routes.seats, " +
            "users_routes.user_id, " +
            "users_routes.route_id, " +
            "users_routes.date_reserve, " +
            "users.name as user_name, " +
            "routes.train_number as route_train_number, " +
            "routes.station_departure_id as station_departure_id, " +
            "routes.station_arrival_id as station_arrival_id, " +
            "routes.travel_cost as route_travel_cost, " +
            "routes.seats_total as route_seats_total, " +
            "sum(ur.seats) as route_seats_reserved, " +
            "routes.seats_total - COALESCE(sum(ur.seats), 0) as route_seats_available, " +
            "routes.date_departure as route_date_departure, " +
            "routes.date_arrival as route_date_arrival, " +
            "s_d.name as station_departure_name, " +
            "s_a.name as station_arrival_name " +
            "FROM users_routes " +
            "LEFT JOIN users ON users_routes.user_id = users.id " +
            "LEFT JOIN routes ON users_routes.route_id = routes.id " +
            "LEFT JOIN stations s_a ON routes.station_arrival_id = s_a.id " +
            "LEFT JOIN stations s_d ON routes.station_departure_id = s_d.id " +
            "LEFT JOIN users_routes ur ON routes.id = ur.route_id " +
            "WHERE users_routes.id = ? " +
            "GROUP BY routes.id ";

    private static final String SQL_ADD_USERROUTE = "INSERT " +
            "INTO users_routes (user_id, route_id, seats, date_reserve) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_USERROUTE = "UPDATE " +
            "users_routes SET user_id = ?, route_id = ?, seats = ?, date_reserve = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_USERROUTE = "DELETE FROM " +
            "users_routes WHERE id = ?";


    @Override
    public ArrayList<UserRoute> getAll() throws DBException {

        ArrayList<UserRoute> usersRoutes = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_USERSROUTES);


            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                User user = new User.Builder()
                        .withId(resultSet.getInt(Station.F_ID))
                        .withName(resultSet.getString(Station.F_NAME))
                        .build();

                Station station_departure = new Station.Builder()
                        .withId(resultSet.getInt("station_departure_id"))
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station station_arrival = new Station.Builder()
                        .withId(resultSet.getInt("station_arrival_id"))
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                Route route = new Route(
                        resultSet.getInt("route_id"),
                        resultSet.getString("route_train_number"),
                        station_departure,
                        station_arrival,
                        resultSet.getString("route_date_departure"),
                        resultSet.getString("route_date_arrival"),
                        resultSet.getInt("route_travel_cost"),
                        resultSet.getInt("route_seats_available"),
                        resultSet.getInt("route_seats_total")
                );

                UserRoute userRoute = new UserRoute(
                        resultSet.getInt("id"),
                        user,
                        route,
                        resultSet.getInt("seats"),
                        resultSet.getString("date_reserve"));

                usersRoutes.add(userRoute);

                //            logger.info("! id: " + Route.getId() + "; name: " + Route.getName());
            }

        } catch (SQLException e) {
            logger.error("SQLException while update(id, user): {}", e.getMessage());
            throw new DBException("SQLException while update(id, user)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeResultSet(resultSet);
            DBManager.closeConnection(connection);
        }

        return usersRoutes;
    }

    public ArrayList<UserRoute> getFiltered(ArrayList<FilterQuery> filters) throws DBException {

        logger.trace("#getFiltered()");
        logger.info("filters: " + filters);

        ArrayList<UserRoute> usersRoutes = new ArrayList<>();

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        StringBuilder sb = new StringBuilder(SQL_GET_ALL_USERSROUTES);
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
                sb.append("users_routes.").append(filter.getField()).append(" IN (");

                sb.append(filter.getValues().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))).append(")");
            }

        }
        logger.info("getFiltered -- SQL_GET_ALL_USERSROUTES_FILTERED: " + sb);

        try {
            preparedStatement = connection.prepareStatement(sb.toString());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                User user = new User.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .build();

                Station station_departure = new Station.Builder()
                        .withId(resultSet.getInt("station_departure_id"))
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station station_arrival = new Station.Builder()
                        .withId(resultSet.getInt("station_arrival_id"))
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                Route route = new Route(
                        resultSet.getInt("route_id"),
                        resultSet.getString("route_train_number"),
                        station_departure,
                        station_arrival,
                        resultSet.getString("route_date_departure"),
                        resultSet.getString("route_date_arrival"),
                        resultSet.getInt("route_travel_cost"),
                        resultSet.getInt("route_seats_available"),
                        resultSet.getInt("route_seats_total")
                );

                UserRoute userRoute = new UserRoute(
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
            DBManager.closeConnection(connection);
        }

        return usersRoutes;
    }

    @Override
    public UserRoute get(int id) throws DBException {

        logger.trace("#get(id): {}", id);

        UserRoute userRoute = null;

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_USERROUTE_BY_ID);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .build();

                Station station_departure = new Station.Builder()
                        .withId(resultSet.getInt("station_departure_id"))
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station station_arrival = new Station.Builder()
                        .withId(resultSet.getInt("station_arrival_id"))
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                Route route = new Route(
                        resultSet.getInt("route_id"),
                        resultSet.getString("route_train_number"),
                        station_departure,
                        station_arrival,
                        resultSet.getString("route_date_departure"),
                        resultSet.getString("route_date_arrival"),
                        resultSet.getInt("route_travel_cost"),
                        resultSet.getInt("route_seats_available"),
                        resultSet.getInt("route_seats_total")
                );

                userRoute = new UserRoute(
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
            DBManager.closeConnection(connection);
        }

        return userRoute;
    }

    @Override
    public void add(UserRoute userRoute) throws DBException {

        logger.trace("#add(userRoute): {}", userRoute);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_USERROUTE);
            int l = 0;
            preparedStatement.setInt(++l, userRoute.getUser().getId());
            preparedStatement.setInt(++l, userRoute.getRoute().getId());
            preparedStatement.setInt(++l, userRoute.getSeats());
            preparedStatement.setString(++l, userRoute.getDate_reserve());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while add(userRoute): {}", e.getMessage());
            throw new DBException("SQLException while add(userRoute)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

    @Override
    public void update(int id, UserRoute userRoute) throws DBException {

        logger.trace("#update(id, userRoute): {} -- {}", id, userRoute);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USERROUTE);

            int l = 0;
            preparedStatement.setInt(++l, userRoute.getUser().getId());
            preparedStatement.setInt(++l, userRoute.getRoute().getId());
            preparedStatement.setInt(++l, userRoute.getSeats());
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while update(id, userRoute): {}", e.getMessage());
            throw new DBException("SQLException while update(id, userRoute)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

    @Override
    public void delete(int id) throws DBException {

        logger.trace("#delete(id): {}", id);

        DBManager dbManager = DBManager.getInstance();
        Connection connection = dbManager.getConnection();

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_USERROUTE);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while delete(id): {}", e.getMessage());
            throw new DBException("SQLException while delete(id)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
            DBManager.closeConnection(connection);
        }
    }

}
