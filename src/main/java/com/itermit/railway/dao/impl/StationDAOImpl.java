package com.itermit.railway.dao.impl;


import com.itermit.railway.dao.StationDAO;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StationDAOImpl implements StationDAO {
    private DBManager dbManager;
    private static StationDAOImpl instance;
    private static final Logger logger = LogManager.getLogger(StationDAOImpl.class);
    private static final String SQL_GET_ALL_STATIONS = "SELECT id, name FROM stations";
    private static final String SQL_GET_STATIONS_BY_ID = "SELECT id, name FROM stations WHERE id = ?";
    private static final String SQL_ADD_STATION = "INSERT INTO stations (name) VALUES (?)";
    private static final String SQL_UPDATE_STATION = "UPDATE stations SET name = ? WHERE id = ?";
    private static final String SQL_DELETE_STATION = "DELETE FROM stations WHERE id = ?";

    public static synchronized StationDAOImpl getInstance() {
        if (instance == null) {
            instance = new StationDAOImpl();
        }
        return instance;
    }

    private StationDAOImpl() {
        dbManager = DBManager.getInstance();
    }

    @Override
    public ArrayList<Station> getAll() throws DBException {

        logger.debug("#getAll()");

        ArrayList<Station> stations = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            logger.trace(SQL_GET_ALL_STATIONS);
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_STATIONS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                stations.add(new Station.Builder()
                        .withId(resultSet.getInt(User.F_ID))
                        .withName(resultSet.getString(User.F_NAME))
                        .build());
            }

        } catch (SQLException e) {
            logger.error("SQLException while getAll(): {}", e.getMessage());
            throw new DBException("Cannot get items!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return stations;
    }

    @Override
    public Station get(int id) throws DBException {

        logger.debug("#get(id): {}", id);

        Station station = null;

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;

        try (Connection connection = dbManager.getConnection()) {
            logger.trace(SQL_GET_STATIONS_BY_ID);
            preparedStatement = connection.prepareStatement(SQL_GET_STATIONS_BY_ID);
            int l = 0;
            preparedStatement.setInt(++l, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                station = new Station.Builder()
                        .withId(resultSet.getInt(Station.F_ID))
                        .withName(resultSet.getString(Station.F_NAME))
                        .build();
            }
        } catch (SQLException e) {
            logger.error("SQLException while get(id): {}", e.getMessage());
            throw new DBException("Cannot get item!", e);
        } finally {
            DBManager.closeResultSet(resultSet);
            DBManager.closePreparedStatement(preparedStatement);
        }

        return station;
    }

    @Override
    public void add(Station station) throws DBException {

        logger.debug("#add(station): {}", station);

        PreparedStatement preparedStatement = null;
        try (Connection connection = dbManager.getConnection()) {
            logger.trace(SQL_UPDATE_STATION);
            preparedStatement = connection.prepareStatement(SQL_ADD_STATION);
            int l = 0;
            preparedStatement.setString(++l, station.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while add(station): {}", e.getMessage());
            throw new DBException("Cannot add item!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void update(int id, Station station) throws DBException {

        logger.debug("#update(id, station): {} -- {}", id, station);

        PreparedStatement preparedStatement = null;
        try (Connection connection = dbManager.getConnection()) {
            logger.trace(SQL_UPDATE_STATION);
            preparedStatement = connection.prepareStatement(SQL_UPDATE_STATION);
            int l = 0;
            preparedStatement.setString(++l, station.getName());
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while update(id, station): {}", e.getMessage());
            throw new DBException("Cannot update item!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void delete(int id) throws DBException {

        logger.debug("#delete(id): {}", id);

        PreparedStatement preparedStatement = null;
        try (Connection connection = dbManager.getConnection()) {
            logger.trace(SQL_DELETE_STATION);
            preparedStatement = connection.prepareStatement(SQL_DELETE_STATION);
            int l = 0;
            preparedStatement.setInt(++l, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQLException while delete(id): {}", e.getMessage());
            throw new DBException("Cannot delete item!", e);
        } finally {
            DBManager.closePreparedStatement(preparedStatement);
        }
    }

}
