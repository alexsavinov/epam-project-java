package com.itermit.railway.db.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Route implements Serializable {

    private int id;
    private String trainNumber;
    private Station stationDeparture;
    private Station stationArrival;
    private String dateDeparture;
    private String dateArrival;
    private int travelCost;
    private int seatsReserved;
    private int seatsTotal;


    private Route() {
    }

    public Route(int id) {
        this.id = id;
    }

    public String getName() {
        return trainNumber + " ("
                + stationDeparture.getName() + " - "
                + stationArrival.getName() + ")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public Station getStationDeparture() {
        return stationDeparture;
    }

    public void setStationDeparture(Station stationDeparture) {
        this.stationDeparture = stationDeparture;
    }

    public Station getStationArrival() {
        return stationArrival;
    }

    public void setStationArrival(Station stationArrival) {
        this.stationArrival = stationArrival;
    }

    public String getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(String dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public String getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(String dateArrival) {
        this.dateArrival = dateArrival;
    }

    public String getTravelTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
        LocalDateTime date1 = LocalDateTime.parse(dateDeparture, formatter);
        LocalDateTime date2 = LocalDateTime.parse(dateArrival, formatter);

        Duration duration = Duration.between(date1, date2);

//        int seconds = duration.toSecondsPart();
//        int minutes = duration.toMinutesPart();
//        int hours = duration.toHoursPart();
//        long days = duration.toDays();
        long hours = duration.toHours();

        StringBuffer sb = new StringBuffer();
//        if (days != 0) sb.append(days + (days == 1 ? " day " : " days "));
        if (hours != 0) sb.append(hours + (hours == 1 ? " hour " : " hours "));
//        if (minutes != 0) sb.append(minutes + (minutes == 1 ? " minute " : " minutes "));
//        if (seconds != 0) sb.append(seconds + (seconds == 1 ? " second " : " seconds "));

        return sb.toString();

    }

    public int getTravelCost() {
        return travelCost;
    }

    public void setTravelCost(int travelCost) {
        this.travelCost = travelCost;
    }

    public int getSeatsAvailable() {
        return seatsTotal - seatsReserved;
    }

    public int getSeatsReserved() {
        return seatsReserved;
    }

    public void setSeatsReserved(int seatsReserved) {
        this.seatsReserved = seatsReserved;
    }

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(int seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", train_number='" + trainNumber + '\'' +
                ", station_departure='" + stationDeparture + '\'' +
                ", date_departure='" + dateDeparture + '\'' +
                ", station_arrival='" + stationArrival + '\'' +
                ", date_arrival='" + dateArrival + '\'' +
                ", travel_cost='" + travelCost + '\'' +
                ", seats_reserved='" + seatsReserved + '\'' +
                ", seats_total='" + seatsTotal + '\'' +
                '}';
    }

    public static class Builder {
        private final Route route;

        public Builder() {
            route = new Route();
        }

        public Route.Builder withId(int id) {
            route.setId(id);
            return this;
        }

        public Route.Builder withTrainNumber(String name) {
            route.setTrainNumber(name);
            return this;
        }

        public Route.Builder withStationDeparture(Station station) {
            route.setStationDeparture(station);
            return this;
        }

        public Route.Builder withStationArrival(Station station) {
            route.setStationArrival(station);
            return this;
        }

        public Route.Builder withDateDeparture(String date) {
            route.setDateDeparture(date);
            return this;
        }

        public Route.Builder withDateArrival(String date) {
            route.setDateArrival(date);
            return this;
        }

        public Route.Builder withTravelCost(int value) {
            route.setTravelCost(value);
            return this;
        }

        public Route.Builder withSeatsReserved(int value) {
            route.setSeatsReserved(value);
            return this;
        }

        public Route.Builder witheatsTotal(int value) {
            route.setSeatsTotal(value);
            return this;
        }

        public Route build() {
            return route;
        }
    }
}
