package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.RouteDAO;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RouteDAOImpl implements RouteDAO {

    private DBManager dbManager;
    private static RouteDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(RouteDAOImpl.class);
    private static final String SQL_GET_ALL_ROUTES = "SELECT " +
            "routes.id, " +
            "routes.train_number, " +
            "routes.station_departure_id, " +
            "s_d.name as station_departure_name, " +
            "routes.station_arrival_id, " +
            "s_a.name as station_arrival_name, " +
            "routes.date_departure, " +
            "routes.date_arrival, " +
            "routes.travel_cost, " +
            "routes.seats_total as seats_total, " +
            "routes.seats_reserved, " +
            "routes.seats_total - routes.seats_reserved as seats_available " +
            "FROM routes " +
            "LEFT JOIN stations s_a ON s_a.id = routes.station_arrival_id " +
            "LEFT JOIN stations s_d ON s_d.id = routes.station_departure_id";
    private static final String SQL_GET_ROUTE_BY_ID = "SELECT " +
            "routes.id, " +
            "routes.train_number, " +
            "routes.station_departure_id, " +
            "s_d.name as station_departure_name, " +
            "routes.station_arrival_id, " +
            "s_a.name as station_arrival_name, " +
            "routes.date_departure, " +
            "routes.date_arrival, " +
            "routes.travel_cost, " +
            "routes.seats_total, " +
            "routes.seats_reserved " +
            "FROM routes " +
            "LEFT JOIN stations s_a ON s_a.id = routes.station_arrival_id " +
            "LEFT JOIN stations s_d ON s_d.id = routes.station_departure_id " +
            "WHERE routes.id = ?";
    private static final String SQL_ADD_ROUTE = "INSERT " +
            "INTO routes (train_number, station_departure_id, station_arrival_id, " +
            "   date_departure, date_arrival, travel_cost, seats_reserved, seats_total) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_ROUTE = "UPDATE routes " +
            "SET train_number = ?, station_departure_id = ?, station_arrival_id = ?, " +
            "   date_departure = ?, date_arrival = ?, travel_cost = ?, seats_reserved = ?, seats_total = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE_ROUTE = "DELETE FROM routes WHERE id = ?";
    private static final String SQL_GET_TOTAL_ROWS = "SELECT COUNT(*) total_rows FROM routes";
    public static final String SQL_ADD_RESERVE_ROUTE = "UPDATE routes " +
            "SET seats_reserved = seats_reserved + ? " +
            "WHERE id = ?";
    public static final String SQL_REMOVE_RESERVE_ROUTE = "UPDATE routes " +
            "SET seats_reserved = seats_reserved - ? " +
            "WHERE id = ?";
    public static final String SQL_CHECK_RESERVE_ROUTE = "SELECT id " +
            "FROM routes " +
            "WHERE id = ? AND (seats_reserved > seats_total " +
            "   OR seats_reserved < 0 " +
            "   OR seats_total < 0)";

    private RouteDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    public static synchronized RouteDAOImpl getInstance() {
        if (instance == null) {
            instance = new RouteDAOImpl();
        }
        return instance;
    }

    @Override
    public ArrayList<Route> getAll(Connection connection) throws SQLException {

        logger.debug("#getAll()");

        ArrayList<Route> routes = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ROUTES);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                routes.add(extract(resultSet));
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return routes;
    }

    public int getTotalRows(Connection connection, QueryMaker query) throws SQLException {

        int totalRows = 0;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = query.getPreparedStatement(connection);

            ResultSet rsTotalRows = preparedStatement.executeQuery();
            if (rsTotalRows.next()) {
                totalRows = rsTotalRows.getInt("total_rows");
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return totalRows;
    }

    public Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException {

        logger.debug("#getPaginated().");

        ArrayList<Route> routes = getFiltered(connection, query);

        query.setQueryMain(SQL_GET_TOTAL_ROWS);
        query.deleteQueryOffset();

        int totalRows = getTotalRows(connection, query);

//        logger.info("totalRows {}", totalRows);
//        logger.info("query {}", query);

        int total_pages = (int) Math.ceil(totalRows / (float) (Paginator.PAGE_SIZE));
//        logger.info("total_pages {}", total_pages);
        return new Paginator.Builder()
                .withPage(query.getPage())
                .withPages(total_pages)
                .withResults(totalRows)
                .withData(routes)
                .build();
    }

    public ArrayList<Route> getFiltered(Connection connection, QueryMaker query) throws SQLException {

        logger.debug("#getAll().  {}", query);

        ArrayList<Route> routes = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            query.setQueryMain(SQL_GET_ALL_ROUTES);
            preparedStatement = query.getPreparedStatement(connection);
            logger.info("preparedStatement: {}", preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                routes.add(extract(resultSet));
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return routes;
    }

    @Override
    public Route get(Connection connection, int id) throws SQLException {

        logger.debug("#get(id): {}", id);

        Route route = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_GET_ROUTE_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                route = extract(resultSet);
            }
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return route;
    }

    @Override
    public void add(Connection connection, Route route) throws SQLException {

        logger.debug("#add(route): {}", route);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_ROUTE);
            int l = 0;
            preparedStatement.setString(++l, route.getTrainNumber());
            preparedStatement.setInt(++l, route.getStationDeparture().getId());
            preparedStatement.setInt(++l, route.getStationArrival().getId());
            preparedStatement.setString(++l, route.getDateDeparture());
            preparedStatement.setString(++l, route.getDateArrival());
            preparedStatement.setInt(++l, route.getTravelCost());
            preparedStatement.setInt(++l, route.getSeatsReserved());
            preparedStatement.setInt(++l, route.getSeatsTotal());
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void update(Connection connection, int id, Route route) throws SQLException {

        logger.debug("#update(id, route): {} -- {}", id, route);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ROUTE);
            int l = 0;
            preparedStatement.setString(++l, route.getTrainNumber());
            preparedStatement.setInt(++l, route.getStationDeparture().getId());
            preparedStatement.setInt(++l, route.getStationArrival().getId());
            preparedStatement.setString(++l, route.getDateDeparture());
            preparedStatement.setString(++l, route.getDateArrival());
            preparedStatement.setInt(++l, route.getTravelCost());
            preparedStatement.setInt(++l, route.getSeatsReserved());
            preparedStatement.setInt(++l, route.getSeatsTotal());
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void delete(Connection connection, int id) throws SQLException {

        logger.debug("#delete(id): {}", id);

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_ROUTE);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public Route extract(ResultSet resultSet) throws SQLException {

        Station stationDeparture = new Station.Builder()
                .withId(resultSet.getInt("station_departure_id"))
                .withName(resultSet.getString("station_departure_name"))
                .build();

        Station stationArrival = new Station.Builder()
                .withId(resultSet.getInt("station_arrival_id"))
                .withName(resultSet.getString("station_arrival_name"))
                .build();

        return new Route.Builder()
                .withId(resultSet.getInt("id"))
                .withTrainNumber(resultSet.getString("train_number"))
                .withStationDeparture(stationDeparture)
                .withStationArrival(stationArrival)
                .withDateDeparture(resultSet.getString("date_departure"))
                .withDateArrival(resultSet.getString("date_arrival"))
                .withTravelCost(resultSet.getInt("travel_cost"))
                .withSeatsReserved(resultSet.getInt("seats_reserved"))
                .withSeatsTotal(resultSet.getInt("seats_total"))
                .build();
    }

}
