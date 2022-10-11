package com.itermit.railway.dao;


import com.itermit.railway.dao.entity.User;
import com.itermit.railway.db.DBException;

public interface UserDAO extends DAO<User> {

    User get(User user) throws DBException;

}
