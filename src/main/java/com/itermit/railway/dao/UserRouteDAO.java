package com.itermit.railway.dao;


import com.itermit.railway.dao.entity.UserRoute;
import com.itermit.railway.db.DBException;
import com.itermit.railway.utils.FilterQuery;

import java.util.ArrayList;

public interface UserRouteDAO extends DAO<UserRoute> {
    ArrayList<UserRoute> getFiltered(ArrayList<FilterQuery> filters) throws DBException;

}
