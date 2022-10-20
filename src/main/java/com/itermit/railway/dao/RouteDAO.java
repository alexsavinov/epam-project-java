package com.itermit.railway.dao;


import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.DBException;
import com.itermit.railway.utils.FilterQuery;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;

import java.util.ArrayList;

public interface RouteDAO extends DAO<Route> {

    ArrayList<Route> getFiltered(QueryMaker query) throws DBException;
    Paginator getPaginated(QueryMaker query) throws DBException;

}
