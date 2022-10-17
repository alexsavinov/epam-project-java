package com.itermit.railway.dao.impl;

import com.itermit.railway.dao.RouteDAO;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.FilterQuery;
import com.itermit.railway.utils.Paginator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
            "sum(ur.seats) as seats_reserved, " +
            "routes.seats_total - COALESCE(sum(ur.seats), 0) as seats_available " +
            "FROM routes " +
            "LEFT JOIN stations s_a ON s_a.id = routes.station_arrival_id " +
            "LEFT JOIN stations s_d ON s_d.id = routes.station_departure_id " +
            "LEFT JOIN orders ur ON routes.id = ur.route_id group by routes.id";
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

    private static final String SQL_GET_ROUTE_BY_ID___ = "SELECT " +
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
            "sum(ur.seats) as seats_reserved, " +
            "routes.seats_total - COALESCE(sum(ur.seats), 0) as seats_available " +
            "FROM routes " +
            "LEFT JOIN stations s_a ON s_a.id = routes.station_arrival_id " +
            "LEFT JOIN stations s_d ON s_d.id = routes.station_departure_id " +
            "LEFT JOIN orders ur ON routes.id = ur.route_id " +
            "WHERE routes.id = ? " +
            "GROUP BY routes.id ";
    private static final String SQL_ADD_ROUTE = "INSERT INTO routes (train_number, station_departure_id, station_arrival_id, date_departure, date_arrival, travel_cost, seats_total) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_ROUTE = "UPDATE routes SET train_number = ?, station_departure_id = ?, station_arrival_id = ?, date_departure = ?, date_arrival = ?, travel_cost = ?, seats_total = ?  WHERE id = ?";
    private static final String SQL_DELETE_ROUTE = "DELETE FROM routes WHERE id = ?";
    private static final String SQL_GET_TOTAL_ROWS = "SELECT COUNT(*) total_rows FROM routes";


    public static synchronized RouteDAOImpl getInstance() {
        if (instance == null) {
            instance = new RouteDAOImpl();
        }
        return instance;
    }

    private RouteDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public ArrayList<Route> getAll() throws DBException {

        logger.debug("#getAll()");

        ArrayList<Route> routes = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ROUTES);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Station stationArrival = new Station.Builder()
                        .withId(resultSet.getInt("station_departure_id"))
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station stationDeparture = new Station.Builder()
                        .withId(resultSet.getInt("station_arrival_id"))
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                Route route = new Route.Builder()
                        .withId(resultSet.getInt("id"))
                        .withTrainNumber(resultSet.getString("train_number"))
                        .withStationDeparture(stationDeparture)
                        .withStationArrival(stationArrival)
                        .withDateDeparture(resultSet.getString("date_departure"))
                        .withDateArrival(resultSet.getString("date_arrival"))
                        .withTravelCost(resultSet.getInt("travel_cost"))
                        .withSeatsReserved(resultSet.getInt("seats_reserved"))
                        .witheatsTotal(resultSet.getInt("seats_total"))
                        .build();


//                Route route = new Route(
//                        resultSet.getInt("id"),
//                        resultSet.getString("train_number"),
//                        new Station.Builder()
//                                .withId(resultSet.getInt("station_departure_id"))
//                                .withName(resultSet.getString("station_departure_name"))
//                                .build(),
//                        new Station.Builder()
//                                .withId(resultSet.getInt("station_arrival_id"))
//                                .withName(resultSet.getString("station_arrival_name"))
//                                .build(),
//                        resultSet.getString("date_departure"),
//                        resultSet.getString("date_arrival"),
////                    resultSet.getInt("travel_time"),
//                        resultSet.getInt("travel_cost"),
//                        resultSet.getInt("seats_available"),
//                        resultSet.getInt("seats_total")
//                );
                routes.add(route);
            }
        } catch (SQLException e) {
            logger.error("SQLException while getAll(): {}", e.getMessage());
            throw new DBException("SQLException while getAll()!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return routes;
    }

    //    public List<Route> getFiltered(List<FilterQuery> filters) throws SQLException {
    public Paginator getFiltered(ArrayList<FilterQuery> filters) throws DBException {

        logger.info("filters: " + filters);

        logger.debug("#getAll()");

        ArrayList<Route> routes = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Paginator paginator = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ROUTES);
            resultSet = preparedStatement.executeQuery();

//        PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_ROUTES);

//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;

            StringBuilder sb = new StringBuilder(
                    SQL_GET_ALL_ROUTES.replace("group by routes.id", ""));

            StringBuilder sbTotalRows = new StringBuilder(SQL_GET_TOTAL_ROWS);

            StringBuilder sbAfter = new StringBuilder();
//        StringBuilder sbTotalRowsAfter = new StringBuilder();

            String SQL_TOTAL_ROWS = "SELECT COUNT(*) total_rows FROM routes WHERE 1=1";

            Boolean hasJoin = filters.size() > 0 && filters.get(0).getField().equals("seats_available");

            if (hasJoin) {
                SQL_TOTAL_ROWS = "SELECT COUNT(*) total_rows FROM (SELECT seats_total from routes " +
                        "LEFT JOIN orders ur ON routes.id = ur.route_id  " +
                        "WHERE 1=1 " +
                        "group by routes.id HAVING 2=2) as t";
            }

            StringBuilder sbConditions = new StringBuilder();
            StringBuilder sbHaving = new StringBuilder();

            String pg_page = "0";

            boolean first = true;

            for (int i = 0; i < filters.size(); i++) {
                FilterQuery filter = filters.get(i);
                StringBuilder sbCurrent = new StringBuilder();

                String field = filter.getField();

                if (field.equals("pg_page") || field.equals("min_seats")) {

                } else {
                    if (first) {
                        sbCurrent.append(" WHERE ");
                        first = false;
                    } else {
                        sbCurrent.append(" AND ");
                    }
                }

//            if (hasJoin) {
//                if (i > 0) {
//                    sbCurrent.append(i == 1 ? " WHERE " : " AND ");
//                }
//            } else {
//                sbCurrent.append(i == 0 ? " WHERE " : " AND ");
//            }

//            if (i > 1) {
                sbConditions.append(sbCurrent);
//            }

                logger.info("___ sbCurrent: " + sbCurrent + " i:" + i + " hasJoin:"
                        + hasJoin + " filter:" + filter.getField());
//            logger.info("___ sbConditions: " + sbConditions);

                if (filter.getField().equals("date_departure")
                        || filter.getField().equals("date_arrival")
                        || filter.getField().equals("travel_cost")
                        || filter.getField().equals("seats_total")
                        || filter.getField().equals("seats_reserved")) {

                    sbCurrent.append("routes.").append(filter.getField()).append(" ")
                            .append(filter.getCondition()).append(" '")
                            .append(filter.getValues().get(0)).append("'");

                    sbConditions.append("routes.").append(filter.getField()).append(" ")
                            .append(filter.getCondition()).append(" '")
                            .append(filter.getValues().get(0)).append("'");

                } else if (filter.getField().equals("travel_time")) {

                    sbCurrent.append("TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) ")
                            .append(filter.getCondition()).append(" '")
                            .append(filter.getValues().get(0)).append("'");

                    sbConditions.append("TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) ")
                            .append(filter.getCondition()).append(" '")
                            .append(filter.getValues().get(0)).append("'");

                } else if (filter.getField().equals("seats_available")) {

                    sbAfter.append(" HAVING routes.seats_total - COALESCE(sum(ur.seats), 0) ")
                            .append(filter.getCondition()).append(" '")
                            .append(filter.getValues().get(0)).append("'");

                    sbHaving.append(" HAVING routes.seats_total - COALESCE(sum(ur.seats), 0) ")
                            .append(filter.getCondition()).append(" '")
                            .append(filter.getValues().get(0)).append("'");

                } else if (filter.getField().equals("pg_page")) {

                    pg_page = String.valueOf(filter.getValues().get(0));

                    sbAfter.append(" LIMIT ").append(Paginator.PAGE_SIZE)
                            .append(" OFFSET ")
                            .append(Integer.valueOf(pg_page) * Paginator.PAGE_SIZE - Paginator.PAGE_SIZE);

                } else {

                    sbCurrent.append("routes.").append(filter.getField()).append(" IN (");

                    sbCurrent.append(filter.getValues().stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "))).append(")");
                }

                sb.append(sbCurrent);
//            sbTotalRows.append(sbCurrent);

            }
            sb.append(" group by routes.id").append(sbAfter);
//        sbTotalRows.append(sbTotalRowsAfter).append(sbAfter);

            if (sbConditions.length() > 0) {
                SQL_TOTAL_ROWS = SQL_TOTAL_ROWS.replace("WHERE 1=1", sbConditions);
            }
            if (sbHaving.length() > 0) {
                SQL_TOTAL_ROWS = SQL_TOTAL_ROWS.replace("HAVING 2=2", sbHaving);
            }

            logger.info("getFiltered -- SQL_GET_ALL_USERSROUTES_FILTERED: " + sb);
//        logger.info("getFiltered -- sbTotalRows: " + sbTotalRows);
            logger.info("getFiltered -- SQL_TOTAL_ROWS: " + SQL_TOTAL_ROWS);

            preparedStatement = connection.prepareStatement(sb.toString());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Station stationArrival = new Station.Builder()
                        .withId(resultSet.getInt("station_departure_id"))
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station stationDeparture = new Station.Builder()
                        .withId(resultSet.getInt("station_arrival_id"))
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                Route route = new Route.Builder()
                        .withId(resultSet.getInt("id"))
                        .withTrainNumber(resultSet.getString("train_number"))
                        .withStationDeparture(stationDeparture)
                        .withStationArrival(stationArrival)
                        .withDateDeparture(resultSet.getString("date_departure"))
                        .withDateArrival(resultSet.getString("date_arrival"))
                        .withTravelCost(resultSet.getInt("travel_cost"))
                        .withSeatsReserved(resultSet.getInt("seats_reserved"))
                        .witheatsTotal(resultSet.getInt("seats_total"))
                        .build();

                routes.add(route);

            }

            /* Pagination */
//        new Paginator(1, 1, 1, 1, routes);


            PreparedStatement psTotalRows = connection.prepareStatement(SQL_TOTAL_ROWS);

            int total_rows = 0;
            ResultSet rsTotalRows = psTotalRows.executeQuery();
            if (rsTotalRows.next()) {
                total_rows = rsTotalRows.getInt("total_rows");
            }

//        int total_pages = total_rows / Paginator.PAGE_SIZE;
            int total_pages = (int) Math.ceil(total_rows / (float) (Paginator.PAGE_SIZE));

            paginator = new Paginator(Integer.parseInt(pg_page), total_pages, routes);

            logger.info("!== pg_page: " + pg_page);
            logger.info("!++ total_rows: " + total_rows);
            logger.info("!++ total_pages: " + total_pages);
//        logger.info("!++ total_pages2: " + total_rows / Paginator.PAGE_SIZE);
            logger.info("!++ paginator: " + paginator);

        } catch (SQLException e) {
            logger.error("SQLException while getAll(): {}", e.getMessage());
            throw new DBException("SQLException while getAll()!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

//        return Routes;
        return paginator;
    }

    @Override
    public Route get(int id) throws DBException {

        logger.debug("#get(id): {}", id);

        Route route = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_GET_ROUTE_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Station stationArrival = new Station.Builder()
                        .withId(resultSet.getInt("station_departure_id"))
                        .withName(resultSet.getString("station_departure_name"))
                        .build();

                Station stationDeparture = new Station.Builder()
                        .withId(resultSet.getInt("station_arrival_id"))
                        .withName(resultSet.getString("station_arrival_name"))
                        .build();

                route = new Route.Builder()
                        .withId(resultSet.getInt("id"))
                        .withTrainNumber(resultSet.getString("train_number"))
                        .withStationDeparture(stationDeparture)
                        .withStationArrival(stationArrival)
                        .withDateDeparture(resultSet.getString("date_departure"))
                        .withDateArrival(resultSet.getString("date_arrival"))
                        .withTravelCost(resultSet.getInt("travel_cost"))
                        .withSeatsReserved(resultSet.getInt("seats_reserved"))
                        .witheatsTotal(resultSet.getInt("seats_total"))
                        .build();


//                route = new Route(
//                        resultSet.getInt("id"),
//                        resultSet.getString("train_number"),
//                        new Station.Builder()
//                                .withId(resultSet.getInt("station_departure_id"))
//                                .withName(resultSet.getString("station_departure_name"))
//                                .build(),
//                        new Station.Builder()
//                                .withId(resultSet.getInt("station_arrival_id"))
//                                .withName(resultSet.getString("station_arrival_name"))
//                                .build(),
//                        resultSet.getString("date_departure"),
//                        resultSet.getString("date_arrival"),
////                    resultSet.getInt("travel_time"),
//                        resultSet.getInt("travel_cost"),
//                        resultSet.getInt("seats_available"),
//                        resultSet.getInt("seats_total")
//                );
            }
        } catch (SQLException e) {
            logger.error("SQLException while get(id): {}", e.getMessage());
            throw new DBException("SQLException while get(id)!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return route;
    }

    @Override
    public void add(Route route) throws DBException {

        logger.debug("#add(route): {}", route);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_ADD_ROUTE);
            int l = 0;
            preparedStatement.setString(++l, route.getTrainNumber());
            preparedStatement.setInt(++l, route.getStationDeparture().getId());
            preparedStatement.setInt(++l, route.getStationArrival().getId());
            preparedStatement.setString(++l, route.getDateDeparture());
            preparedStatement.setString(++l, route.getDateArrival());
            preparedStatement.setInt(++l, route.getTravelCost());
            preparedStatement.setInt(++l, route.getSeatsTotal());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while add(route): {}", e.getMessage());
            throw new DBException("SQLException while add(route)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void update(int id, Route route) throws DBException {

        logger.debug("#update(id, route): {} -- {}", id, route);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ROUTE);

            int l = 0;
            preparedStatement.setString(++l, route.getTrainNumber());
            preparedStatement.setInt(++l, route.getStationDeparture().getId());
            preparedStatement.setInt(++l, route.getStationArrival().getId());
            preparedStatement.setString(++l, route.getDateDeparture());
            preparedStatement.setString(++l, route.getDateArrival());
            preparedStatement.setInt(++l, route.getTravelCost());
            preparedStatement.setInt(++l, route.getSeatsTotal());
            preparedStatement.setInt(++l, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while update(id, route): {}", e.getMessage());
            throw new DBException("SQLException while update(id, route)!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void delete(int id) throws DBException {

        logger.debug("#delete(id): {}", id);

        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            preparedStatement = connection.prepareStatement(SQL_DELETE_ROUTE);
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
