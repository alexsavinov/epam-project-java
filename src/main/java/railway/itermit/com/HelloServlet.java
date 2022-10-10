package railway.itermit.com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import railway.itermit.com.dao.UserDAO;
import railway.itermit.com.dao.entity.User;
import railway.itermit.com.dao.impl.UserDAOImpl;
import railway.itermit.com.db.DBException;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private static final Logger logger = LogManager.getLogger(HelloServlet.class);

    @Override
    public void init() {
        logger.trace("#init()");

        message = "Hello World!";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        logger.trace("#doGet(request, response)");

        response.setContentType("text/html");


        ArrayList<User> users = new ArrayList<>();

        UserDAO userDAO = new UserDAOImpl();

        try {
            users = userDAO.getAll();
            users.forEach(logger::info);
        } catch (DBException e) {
            logger.error("DBException while update(id, user): {}", e.getMessage());
        }

        try {
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("<ul>");
            users.forEach(s -> out.println("<li>" + s + "</li>"));
            out.println("</ul>");
            out.println("</body></html>");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }
}