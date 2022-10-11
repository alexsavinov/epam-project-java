package com.itermit.railway.dao;


import com.itermit.railway.dao.entity.Route;
import com.itermit.railway.db.DBException;
import com.itermit.railway.utils.FilterQuery;
import com.itermit.railway.utils.Paginator;

import java.util.ArrayList;

public interface RouteDAO extends DAO<Route> {

    Paginator getFiltered(ArrayList<FilterQuery> filters) throws DBException;

}
