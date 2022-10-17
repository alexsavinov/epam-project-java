package com.itermit.railway.command;

import com.itermit.railway.dao.impl.StationDAOImpl;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StationAddPostCommand implements Command {

    private static final Logger logger = LogManager.getLogger(StationAddPostCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

        String name = request.getParameter("name");

        Station station = new Station.Builder().withName(name).build();
        StationDAOImpl.getInstance().add(station);

        request.getSession().setAttribute("messages", "Station " + name + " added!");

        try {
            response.sendRedirect("/stations");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting!", e);
        }

        return null;
    }

}
