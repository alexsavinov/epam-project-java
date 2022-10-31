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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

//import static org.junit.Assert.assertNull;
import org.junit.jupiter.api.Assertions;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import static com.itermit.railway.dao.impl.UserDAOImpl.SQL_GET_ALL_USERS;
import static com.itermit.railway.db.Fields.*;


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

//        final UserDAOImpl implDAO = mock(UserDAOImpl.class);


        ResultSet rs = mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt(ENTITY_ID)).thenReturn(1);
        when(rs.getString(USER_NAME)).thenReturn("admin");
        when(rs.getString(USER_PASSWORD)).thenReturn("admin");
        when(rs.getString(USER_EMAIL)).thenReturn("111");
        when(rs.getBoolean(USER_IS_ADMIN)).thenReturn(true);
        when(rs.getBoolean(USER_IS_ACTIVE)).thenReturn(true);
        when(rs.getString(USER_ACTIVATION_TOKEN)).thenReturn("1232311");

        PreparedStatement pstmt = mock(PreparedStatement.class);
//        pstmt.setInt(1, 1);
        when(pstmt.executeQuery()).thenReturn(rs);

        Connection con = mock(Connection.class);
        when(con.prepareStatement(UserDAOImpl.SQL_GET_USER_BY_ID)).thenReturn(pstmt);

        User expectedUser = new User.Builder().withName("admin").withPassword("admin").build();
//        expectedUser.setLogin("obama");
//        expectedUser.setPassword("obamapass");

//        User actualUser = implDAO.get(con, 1);
        User actualUser = UserDAOImpl.getInstance().get(con, 1);

        System.out.println(actualUser);
        System.out.println(expectedUser);

//        Assertions.assertEquals(null, actualUser);
        Assertions.assertTrue(
                new ReflectionEquals(expectedUser, "id")
                        .matches(actualUser));


//        ArrayList<User> resultGetAll = implDAO.getAll(connection);
//        Assertions.assertEquals(resultGetAll, new ArrayList<User>());
    }


}

