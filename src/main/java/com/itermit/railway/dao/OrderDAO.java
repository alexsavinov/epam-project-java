package com.itermit.railway.dao;


import com.itermit.railway.dao.entity.Order;
import com.itermit.railway.db.DBException;
import com.itermit.railway.utils.FilterQuery;

import java.util.ArrayList;

public interface OrderDAO extends DAO<Order> {
    ArrayList<Order> getFiltered(ArrayList<FilterQuery> filters) throws DBException;

}
