package com.itermit.railway.db;

import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class RouteManager {

    private static RouteManager instance;
    private static final Logger logger = LogManager.getLogger(RouteManager.class);
    private DBManager dbManager;

    private RouteManager() {
        dbManager = DBManager.getInstance();
    }

    public static synchronized RouteManager getInstance() {
        if (instance == null) {
            instance = new RouteManager();
        }
        return instance;
    }

    public ArrayList<Route> getAll() throws DBException {
        logger.debug("#getAll()");

        try (Connection connection = dbManager.getConnection()) {
            return RouteDAOImpl.getInstance().getAll(connection);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while get routes list!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public Route get(int id) throws DBException {
        logger.debug("#get(route).");

        try (Connection connection = dbManager.getConnection()) {
            return RouteDAOImpl.getInstance().get(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void add(Route route) throws DBException {
        logger.debug("#add(route).");

        try (Connection connection = dbManager.getConnection()) {
            RouteDAOImpl.getInstance().add(connection, route);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while adding route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void delete(int id) throws DBException {
        logger.debug("#delete(id).");

        try (Connection connection = dbManager.getConnection()) {
            RouteDAOImpl.getInstance().delete(connection, id);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while deleting route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public void update(int id, Route route) throws DBException {
        logger.debug("#update(id, route): {} -- {}", id, route);

        try (Connection connection = dbManager.getConnection()) {
            RouteDAOImpl.getInstance().update(connection, id, route);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while updating route!", e);
            throw new DBException(errorMessage, e);
        }
    }

    public Paginator getPaginated(QueryMaker queryMaker) throws DBException {
        logger.debug("#getPaginated(queryMaker).");

        try (Connection connection = dbManager.getConnection()) {
            return RouteDAOImpl.getInstance().getPaginated(connection, queryMaker);
        } catch (SQLException e) {
            String errorMessage = CommandContainer.getErrorMessage("Error while getting paginated routes!", e);
            throw new DBException(errorMessage, e);
        }
    }
}
