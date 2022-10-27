package com.itermit.railway.tags;

import java.io.IOException;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public class RouteName extends SimpleTagSupport {

    private String trainNumber;
    private String stationDeparture;
    private String stationArrival;

    /*
     * Example of usage:
     *    <customTags:routeName train_number="001" station_arrival="Station1" station_departure="Station2"/>
     */

    @Override
    public void doTag() throws IOException {
        getJspContext().getOut().print(
                trainNumber + " (" + stationDeparture + " - " + stationArrival + ")"
        );
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public void setStationDeparture(String stationDeparture) {
        this.stationDeparture = stationDeparture;
    }

    public void setStationArrival(String stationArrival) {
        this.stationArrival = stationArrival;
    }

}