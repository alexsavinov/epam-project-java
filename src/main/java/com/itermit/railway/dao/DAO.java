package com.itermit.railway.dao;

import com.itermit.railway.db.DBException;

import java.util.ArrayList;

public interface DAO<T> {

    ArrayList<T> getAll() throws DBException;

    T get(int id) throws DBException;

    void add(T t) throws DBException;

    void update(int id, T t) throws DBException;

    void delete(int id) throws DBException;

}
