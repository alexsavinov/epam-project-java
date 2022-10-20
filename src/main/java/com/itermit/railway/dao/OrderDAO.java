package com.itermit.railway.dao;


import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.DBException;
import com.itermit.railway.utils.QueryMaker;

import java.util.ArrayList;

public interface OrderDAO extends DAO<Order> {
    ArrayList<Order> getFiltered(QueryMaker query) throws DBException;

}
