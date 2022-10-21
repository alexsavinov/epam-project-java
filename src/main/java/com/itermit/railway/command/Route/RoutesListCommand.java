package com.itermit.railway.command.Route;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.RouteManager;
import com.itermit.railway.db.entity.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class RoutesListCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RoutesListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        ArrayList<Route> routes = RouteManager.getInstance().getAll();
        request.setAttribute("routes", routes);

        try {
            request.getRequestDispatcher("/routes.jsp").forward(request, response);
        } catch (ServletException e) {
            logger.error("ServletException. Error routes listing! {}", e.getMessage());
            throw new DBException("Error routes listing!", e);
        } catch (IOException e) {
            logger.error("IOException. Error routes listing! {}", e.getMessage());
            throw new DBException("Error routes listing!", e);
        }

        return null;
    }

}
