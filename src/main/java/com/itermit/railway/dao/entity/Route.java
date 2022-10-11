package com.itermit.railway.dao.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Route implements Serializable {
    private int id;
    private String train_number;
    private Station station_departure;
    private Station station_arrival;
    private String date_departure;
    private String date_arrival;
    //    private int travel_time;
    private int travel_cost;
    private int seats_available;
    private int seats_total;

    public Route(int id, String train_number,
                 Station station_departure, Station station_arrival,
                 String date_departure, String date_arrival,
//                 int travel_time,
                 int travel_cost,
                 int seats_available,
                 int seats_total) {

        this.id = id;
        this.train_number = train_number;
        this.station_departure = station_departure;
        this.station_arrival = station_arrival;
        this.date_departure = date_departure;
        this.date_arrival = date_arrival;
//        this.travel_time = travel_time;
        this.travel_cost = travel_cost;
        this.seats_available = seats_available;
        this.seats_total = seats_total;
    }

    public Route(int id) {
        this.id = id;
    }

    public String getName() {
        return train_number + " ("
                + station_departure.getName() + " - "
                + station_arrival.getName() + ")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public Station getStation_departure() {
        return station_departure;
    }

    public void setStation_departure(Station station_departure) {
        this.station_departure = station_departure;
    }

    public Station getStation_arrival() {
        return station_arrival;
    }

    public void setStation_arrival(Station station_arrival) {
        this.station_arrival = station_arrival;
    }

    public String getDate_departure() {
        return date_departure;
    }

    public void setDate_departure(String date_departure) {
        this.date_departure = date_departure;
    }

    public String getDate_arrival() {
        return date_arrival;
    }

    public void setDate_arrival(String date_arrival) {
        this.date_arrival = date_arrival;
    }

    public String getTravel_time() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime date1 = LocalDateTime.parse(getDate_departure(), formatter);
        LocalDateTime date2 = LocalDateTime.parse(getDate_arrival(), formatter);

        Duration duration = Duration.between(date1, date2);

        int seconds = duration.toSecondsPart();
        int minutes = duration.toMinutesPart();
        int hours = duration.toHoursPart();
        long days = duration.toDays();

        StringBuffer sb = new StringBuffer();
        if (days != 0) sb.append(days + (days == 1 ? " day " : " days "));
        if (hours != 0) sb.append(hours + (hours == 1 ? " hour " : " hours "));
        if (minutes != 0) sb.append(minutes + (minutes == 1 ? " minute " : " minutes "));
        if (seconds != 0) sb.append(seconds + (seconds == 1 ? " second " : " seconds "));

        return sb.toString();

//****  To create a formatted string out a LocalDateTime object you can use the format() method.
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime dateTime = LocalDateTime.of(1986, Month.APRIL, 8, 12, 30);
//        String formattedDateTime = dateTime.format(formatter); // "1986-04-08 12:30"

    }

//    public void setTravel_time(int travel_time) {
//        this.travel_time = travel_time;
//    }

    public int getTravel_cost() {
        return travel_cost;
    }

    public void setTravel_cost(int travel_cost) {
        this.travel_cost = travel_cost;
    }

    public int getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(int seats_available) {
        this.seats_available = seats_available;
    }

    public int getSeats_total() {
        return seats_total;
    }

    public void setSeats_total(int seats_total) {
        this.seats_total = seats_total;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", train_number='" + train_number + '\'' +
                ", station_departure='" + station_departure + '\'' +
                ", date_departure='" + date_departure + '\'' +
                ", station_arrival='" + station_arrival + '\'' +
                ", date_arrival='" + date_arrival + '\'' +
//                ", travel_time='" + travel_time + '\'' +
                ", travel_cost='" + travel_cost + '\'' +
//                ", seats_available='" + seats_available + '\'' +
                ", seats_total='" + seats_total + '\'' +
                '}';
    }
}
