package com.itermit.railway.dao;

import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO<T> {

    ArrayList<T> getAll(Connection connection) throws SQLException;

    Paginator getPaginated(Connection connection, QueryMaker query) throws SQLException;

    ArrayList<T> getFiltered(Connection connection, QueryMaker query) throws SQLException;

    T get(Connection connection, int id) throws SQLException;

    void add(Connection connection, T t) throws SQLException;

    void update(Connection connection, int id, T t) throws SQLException;

    void delete(Connection connection, int id) throws SQLException;

    T extract(ResultSet resultSet) throws SQLException;

}
