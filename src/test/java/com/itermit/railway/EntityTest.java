package com.itermit.railway;

import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Order;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import com.itermit.railway.db.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;


public class EntityTest extends Mockito {

    @Test
    public void UserTest() throws NoSuchAlgorithmException {

        User user = new User.Builder()
                .withId(1)
                .withName("test")
                .withPassword("test1")
                .withEmail("test@test.com")
                .withIsActive(true)
                .withIsAdmin(false)
                .withActivationToken("12345")
                .build();

        user.setIsAdmin(false);
        user.setActive(true);

        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("test", user.getName());
        Assertions.assertEquals("test@test.com", user.getEmail());
        Assertions.assertEquals(true, user.getIsActive());
        Assertions.assertEquals(false, user.getIsAdmin());
        Assertions.assertEquals("12345", user.getActivationToken());
        Assertions.assertEquals("test1", user.getPassword());
        Assertions.assertEquals("b5bea41b6c623f7c09f1bf24dcae58ebab3c0cdd90ad966bc43a45b44867e12b", user.passwordEncrypt("true"));
        Assertions.assertEquals("User{id=1, name='test', password='test1', " +
                "email='test@test.com', isadmin='false', " +
                "isactive='true', activation_token='12345'}", user.toString());

        User userMock = mock(User.class);

        doThrow(new RuntimeException()).when(userMock).generateActivateToken();
        user.generateActivateToken();

//        doThrow(new Exception()).doNothing().when(userMock).generateActivateToken();
    }

    @Test
    public void StationTest() {

        Station station = new Station.Builder()
                .withId(1)
                .withName("test")
                .build();

        Assertions.assertEquals(1, station.getId());
        Assertions.assertEquals("test", station.getName());
        Assertions.assertEquals("Station{id=1, name='test'}", station.toString());
    }

    @Test
    public void OrderTest() {

        Order order = new Order.Builder()
                .withId(1)
                .withRoute(new Route.Builder().build())
                .withUser(new User.Builder().build())
                .withDefaultDateReserve()
                .withSeats(1)
                .withDateReserve(null)
                .build();

        order.setDateReserve(null);

        Assertions.assertEquals(1, order.getId());
        Assertions.assertEquals(1, order.getSeats());
        Assertions.assertEquals(0, order.getRoute().getId());
        Assertions.assertEquals(0, order.getUser().getId());
        Assertions.assertEquals(null, order.getDateReserve());
        Assertions.assertEquals("Order{user=User{id=0, name='null', password='null', " +
                "email='null', isadmin='false', isactive='false', activation_token='null'}, " +
                "route=Station{id=0, train_number='null', station_departure='null', date_departure='null', " +
                "station_arrival='null', date_arrival='null', travel_cost='0', seats_reserved='0', " +
                "seats_total='0'}, seats=1, dateReserve=null}", order.toString());
    }

    @Test
    public void RouteTest() {

        Route route = new Route.Builder()
                .withId(1)
                .withTrainNumber("111")
                .withStationDeparture(new Station.Builder().build())
                .withStationArrival(new Station.Builder().build())
                .withDateDeparture("2022-11-10 00:00:00")
                .withDateArrival("2022-11-11 00:00:00")
                .withTravelCost(123)
                .withSeatsReserved(1232)
                .withSeatsTotal(1231)
                .build();

        Assertions.assertEquals(1, route.getId());
        Assertions.assertEquals("111 (null - null)", route.getName());
        Assertions.assertEquals("111", route.getTrainNumber());
        Assertions.assertEquals("2022-11-10 00:00:00", route.getDateDeparture());
        Assertions.assertEquals("2022-11-11 00:00:00", route.getDateArrival());
        Assertions.assertEquals(0, route.getStationDeparture().getId());
        Assertions.assertEquals(0, route.getStationArrival().getId());
        Assertions.assertEquals("24 hours", route.getTravelTime());
        Assertions.assertEquals(123, route.getTravelCost());
        Assertions.assertEquals(1232, route.getSeatsReserved());
        Assertions.assertEquals(1231, route.getSeatsTotal());
        Assertions.assertEquals(-1, route.getSeatsAvailable());
        Assertions.assertEquals("Station{id=1, train_number='111', " +
                "station_departure='Station{id=0, name='null'}', date_departure='2022-11-10 00:00:00', " +
                "station_arrival='Station{id=0, name='null'}', date_arrival='2022-11-11 00:00:00', " +
                "travel_cost='123', seats_reserved='1232', seats_total='1231'}", route.toString());
    }

}

