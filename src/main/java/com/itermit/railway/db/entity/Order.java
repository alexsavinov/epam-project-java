package com.itermit.railway.db.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Order implements Serializable {
    private int id;
    private User user;
    private Route route;
    private int seats;
    private String dateReserve;

    public Order(int id, User user, Route route, int seats, String dateReserve) {
        this.id = id;
        this.user = user;
        this.route = route;
        this.seats = seats;
        this.dateReserve = dateReserve;
    }

    public Order(int userId, int routeId, int seats) {
        this.user = new User.Builder().withId(userId).build();
        this.route = new Route(routeId);
        this.seats = seats;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd hh:mm:ss",
                Locale.ENGLISH);
        this.dateReserve = dateTimeFormatter.format(LocalDateTime.now());
    }

    public String getDateReserve() {
        return dateReserve;
    }

    public void setDateReserve(String dateReserve) {
        this.dateReserve = dateReserve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user +
                ", route=" + route +
                ", seats=" + seats +
                '}';
    }
}
