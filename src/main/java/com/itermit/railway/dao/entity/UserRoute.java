package com.itermit.railway.dao.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserRoute implements Serializable {
    private int id;
    private User user;
    private Route route;
    private int seats;
    private String date_reserve;

    public UserRoute(int id, User user, Route route, int seats, String date_reserve) {
        this.id = id;
        this.user = user;
        this.route = route;
        this.seats = seats;
        this.date_reserve = date_reserve;
    }

    public UserRoute(int user_id, int route_id, int seats) {
        this.id = 0;
        this.user = new User.Builder().withId(user_id).build();
        this.route = new Route(route_id);
        this.seats = seats;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd hh:mm:ss",
                Locale.ENGLISH);
        this.date_reserve = dateTimeFormatter.format(LocalDateTime.now());
    }

    public String getDate_reserve() {
        return date_reserve;
    }

    public void setDate_reserve(String date_reserve) {
        this.date_reserve = date_reserve;
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
        return "UserRoute{" +
                "user=" + user +
                ", route=" + route +
                ", seats=" + seats +
                '}';
    }
}
