package com.itermit.railway.command.Reserve;

import com.itermit.railway.command.Command;
import com.itermit.railway.db.DBException;
import com.itermit.railway.db.OrderManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReserveDeleteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReserveDeleteCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws DBException {

        logger.debug("#execute(request, response).  {}", request.getRequestURI());

//        int id = CommandContainer.getIdFromRequest(request);

        int id =  Integer.parseInt(request.getParameter("order_id"));
        int seats =  Integer.parseInt(request.getParameter("seats"));

//        logger.info(seats);

        OrderManager.getInstance().deleteReserve(id, seats);

        request.getSession().setAttribute("messages", "Reserve deleted!");

        try {
            response.sendRedirect("/reserves");
        } catch (IOException e) {
            logger.error("IOException. Error redirecting! {}", e.getMessage());
            throw new DBException("Error redirecting /stations!", e);
        }

        return null;
    }

}
