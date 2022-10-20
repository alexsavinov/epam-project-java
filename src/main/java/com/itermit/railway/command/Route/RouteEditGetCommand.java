package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.command.CommandContainer;
import com.itermit.railway.dao.impl.RouteDAOImpl;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Route;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class RouteEditGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteEditGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        int id = CommandContainer.getIdFromRequest(request);

        ArrayList<Station> stations = StationDAOImpl.getInstance().getAll();
        Route route = RouteDAOImpl.getInstance().get(id);

        request.setAttribute("route", route);
        request.setAttribute("stations", stations);
        request.setAttribute("action", "edit");

        try {
            request.getRequestDispatcher("/route.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error route editing! {}", e.getMessage());
            throw new DBException("Error route editing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error route editing! {}", e.getMessage());
            throw new DBException("Error route editing!", e);
        }

        return null;
    }

}
