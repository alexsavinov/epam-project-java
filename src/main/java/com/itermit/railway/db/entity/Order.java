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


    private Order() {
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
                ", dateReserve=" + dateReserve +
                '}';
    }

    public static class Builder {

        private final Order order;

        public Builder() {
            order = new Order();
        }

        public Order.Builder withId(int id) {
            order.setId(id);
            return this;
        }

        public Order.Builder withUser(User user) {
            order.setUser(user);
            return this;
        }

        public Order.Builder withRoute(Route route) {
            order.setRoute(route);
            return this;
        }

        public Order.Builder withSeats(int value) {
            order.setSeats(value);
            return this;
        }

        public Order.Builder withDateReserve(String date) {
            order.setDateReserve(date);
            return this;
        }

        public Order.Builder withDefaultDateReserve() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd hh:mm:ss",
                    Locale.ENGLISH);
            order.setDateReserve(dateTimeFormatter.format(LocalDateTime.now()));
            return this;
        }

        public Order build() {
            return order;
        }
    }

}
