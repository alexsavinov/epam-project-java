package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class StationManager {

    private static StationManager instance;
    private static final Logger logger = LogManager.getLogger(StationManager.class);
    private DBManager dbManager;

    private StationManager() {
        dbManager = DBManager.getInstance();
    }

    public static synchronized StationManager getInstance() {
        if (instance == null) {
            instance = new StationManager();
        }
        return instance;
    }

    public ArrayList<Station> getAll() throws DBException {
        logger.debug("#getAll()");

        try (Connection connection = dbManager.getConnection()) {
            return StationDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get stations list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public Station get(int id) throws DBException {
        logger.debug("#get(station).");

        try (Connection connection = dbManager.getConnection()) {
            return StationDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting station!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void add(Station station) throws DBException {
        logger.debug("#add(station).");

        try (Connection connection = dbManager.getConnection()) {
            StationDAOImpl.getInstance().add(connection, station);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while adding station!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void delete(int id) throws DBException {
        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            StationDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting station!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void update(int id, Station station) throws DBException {
        logger.debug("#update(id, station): {} -- {}", id, station);

        try (Connection connection = dbManager.getConnection()) {
            StationDAOImpl.getInstance().update(connection, id, station);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting station!", e);
            throw new DBException(errorMessage, e);
        }
    }

}
