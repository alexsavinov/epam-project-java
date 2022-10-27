package com.itermit.railway;

//import com.itermit.railway.controller.CommandHandler;
//import com.itermit.railway.db.UserManager;
//import com.itermit.railway.db.entity.User;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "StationServlet",
//        urlPatterns = {"/stations", "/stations/delete/*", "/stations/edit/*", "/stations/add"})
public class ttt extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get parameters
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        try {

            // Load the database driver
//            Class.forName("com.mysql.jdbc.Driver");

            //pass reg details to datamanager
//            dataManager = new DataManager();
            //store result as string
//            String result = dataManager.register(userName, password, name);
//            User user = UserManager.getInstance().get(1);
//            CommandHandler.processRedirect("searchReset", request, response);
            String result = "sfsdfs";

            //set response to html + no cache
            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-cache");
            //send response with register result
            response.getWriter().write(result);

        } catch (Exception e) {
            System.out.println("Exception is :" + e);
        }
    }


}