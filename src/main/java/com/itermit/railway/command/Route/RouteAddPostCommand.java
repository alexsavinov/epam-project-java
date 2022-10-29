package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to add Route.
 * <p>
 * Processes POST-Request.
 * Adds new Route to database.
 *
 * @author O.Savinov
 */
public class RouteAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteAddPostCommand.class);

    /**
     * Command execution.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Address string
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException {

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
                .withSeatsTotal(seatsTotal)
                .build();

        try {
            RouteManager.getInstance().add(route);
        } catch (DBException e) {
            logger.error("DBException. {}", e.getMessage());
            throw new CommandException(e.getMessage(), e);
        }

        request.getSession().setAttribute("messages", "Route " + trainNumber + " added!");

        return "/routes";
    }

}
