package com.itermit.railway.dao;

import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * General DAO interface.
 *
 * @author O.Savinov
 */
public interface DAO<T> {

    /**
     * Returns a list of entities.
     *
     * @param connection Connection to DataSource
     * @return ArrayList of entities
     * @throws SQLException
     */
    ArrayList<T> getAll(Connection connection) throws SQLException;

    /**
     * Returns wrapped by Paginator list of entities.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return Paginated list of entities
     * @throws SQLException
     */
    Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException;

    /**
     * Returns a filtered list of entities.
     *
     * @param connection Connection to DataSource
     * @param query      QueryMaker to construct SQL-query with conditions
     * @return ArrayList of entities
     * @throws SQLException
     */
    ArrayList<T> getFiltered(Connection connection, QueryMaker query) throws SQLException;

    /**
     * Returns an entity by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of entity id
     * @return Entity
     * @throws SQLException
     */
    T get(Connection connection, int id) throws SQLException;

    /**
     * Adds a new entity.
     *
     * @param connection Connection to DataSource
     * @param t          entity to add
     * @throws SQLException
     */
    void add(Connection connection, T t) throws SQLException;

    /**
     * Updates existed entity.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of entity id
     * @param t          entity data to update
     * @throws SQLException
     */
    void update(Connection connection, int id, T t) throws SQLException;

    /**
     * Deletes existed entity by id.
     *
     * @param connection Connection to DataSource
     * @param id         Integer value of entity id
     * @throws SQLException
     */
    void delete(Connection connection, int id) throws SQLException;

    /**
     * Extracts entity data from given ResultSet.
     *
     * @param resultSet ResultSet to process
     * @return entity
     * @throws SQLException
     */
    T extract(ResultSet resultSet) throws SQLException;

}
