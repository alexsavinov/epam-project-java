package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class RouteAddGetCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RouteAddGetCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        ArrayList<Station> stations = StationDAOImpl.getInstance().getAll();
        request.setAttribute("stations", stations);
        request.setAttribute("action", "add");

        try {
            request.getRequestDispatcher("/route.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error routes add! {}", e.getMessage());
            throw new DBException("Error routes add!", e);
        } catch (IOException e) {
            logger.error("IOException. Error routes add! {}", e.getMessage());
            throw new DBException("Error routes add!", e);
        }

        return null;
    }

}
