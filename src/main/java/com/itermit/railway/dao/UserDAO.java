package com.itermit.railway.dao;


import com.itermit.railway.db.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDAO extends DAO<User> {

//    User get(User user) throws SQLException;

    User get(Connection connection, User user) throws SQLException;


}
