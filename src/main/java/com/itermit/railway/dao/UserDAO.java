package com.itermit.railway.dao;

import com.itermit.railway.db.entity.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User DAO interface.
 *
 * @author O.Savinov
 */
public interface UserDAO extends DAO<User> {

    /**
     * Returns a User by entity data.
     *
     * @param connection Connection to DataSource
     * @param user       User entity
     * @return User
     * @throws SQLException
     */
    User get(Connection connection, User user) throws SQLException;

}
