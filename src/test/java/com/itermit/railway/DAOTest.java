package com.itermit.railway;

import com.itermit.railway.dao.impl.OrderDAOImpl;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.dao.impl.UserDAOImpl;
import com.itermit.railway.db.DBManager;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.entity.User;
import com.itermit.railway.utils.Paginator;
import com.itermit.railway.utils.QueryMaker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;

//import static org.junit.Assert.assertNull;
import org.junit.jupiter.api.Assertions;


public class DAOTest extends Mockito {

    @Test
    public void OrderDAOImplTest() throws Exception {

        final OrderDAOImpl orderDAO = mock(OrderDAOImpl.class);
        Connection connection = mock(Connection.class);

        ArrayList<Order> result = orderDAO.getAll(connection);

        Assertions.assertEquals(result, new ArrayList<Order>());

    }

    @Test
    public void RouteDAOImplTest() throws Exception {

        final RouteDAOImpl implDAO = mock(RouteDAOImpl.class);
        Connection connection = mock(Connection.class);

        ArrayList<Route> resultGetAll = implDAO.getAll(connection);
        Assertions.assertEquals(resultGetAll, new ArrayList<Route>());

        int resultЕotalRows = implDAO.getTotalRows(connection, new QueryMaker.Builder().build());
        Assertions.assertEquals(resultЕotalRows, 0);

        Paginator resultPaginator = implDAO.getPaginated(connection, new QueryMaker.Builder().build());
        Assertions.assertEquals(resultPaginator, null);

//        RouteDAOImpl routeDAO = implDAO.getInstance();
//        Assertions.assertEquals(routeDAO, RouteDAOImpl.getInstance());
    }

    @Test
    public void StationDAOImplTest() throws Exception {

        final StationDAOImpl implDAO = mock(StationDAOImpl.class);
        Connection connection = mock(Connection.class);

        ArrayList<Station> resultGetAll = implDAO.getAll(connection);
        Assertions.assertEquals(resultGetAll, new ArrayList<Station>());
    }

    @Test
    public void UserDAOImplTest() throws Exception {

        final UserDAOImpl implDAO = mock(UserDAOImpl.class);
        Connection connection = mock(Connection.class);

        ArrayList<User> resultGetAll = implDAO.getAll(connection);
        Assertions.assertEquals(resultGetAll, new ArrayList<User>());
    }

}

