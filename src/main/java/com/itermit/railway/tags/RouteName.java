package com.itermit.railway.tags;

import java.io.IOException;

import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Simple tag for display Route description in short name.
 * <p>
 * Consist from Train Number and Stations of departure and arrival.
 *
 * @author O.Savinov
 */
public class RouteName extends SimpleTagSupport {

    private String trainNumber;
    private String stationDeparture;
    private String stationArrival;

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