package com.itermit.railway.command;

import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RouteAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteAddPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String trainNumber = CommandContainer.getStringFromRequest(request, "train_number");
        Integer stationDepartureId = CommandContainer.getIntegerFromRequest(request, "station_departure");
        Integer stationArrivalId = CommandContainer.getIntegerFromRequest(request, "station_arrival");
        String dateDeparture = CommandContainer.getStringFromRequest(request, "date_departure");
        String dateArrival = CommandContainer.getStringFromRequest(request, "date_arrival");
        int travelCost = CommandContainer.getIntegerFromRequest(request, "travel_cost");
        int seatsReserved = CommandContainer.getIntegerFromRequest(request, "seats_reserved");
        int seatsTotal = CommandContainer.getIntegerFromRequest(request, "seats_total");

        Route route = new Route.Builder()
                .withTrainNumber(trainNumber)
                .withStationDeparture(new Station.Builder().withId(stationDepartureId).build())
                .withStationArrival(new Station.Builder().withId(stationArrivalId).build())
                .withDateDeparture(dateDeparture)
                .withDateArrival(dateArrival)
                .withTravelCost(travelCost)
                .withSeatsReserved(seatsReserved)
                .witheatsTotal(seatsTotal)
                .build();

//        Route route = new Route(
//                0,
//                train_number,
//                new Station.Builder().withId(station_departure_id).build(),
//                new Station.Builder().withId(station_arrival_id).build(),
//                date_departure,
//                date_arrival,
//                travel_cost,
//                seats_total,
//                seats_total
//        );
        RouteDAOImpl.getInstance().add(route);

        request.getSession().setAttribute("messages", "Route " + trainNumber + " added!");

        try {
            response.sendRedirect("/routes");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}