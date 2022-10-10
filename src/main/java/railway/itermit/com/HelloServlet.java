package railway.itermit.com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private static final Logger logger = LogManager.getLogger(HelloServlet.class);

    @Override
    public void init() {
        logger.info("#init");
        message = "Hello World!";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        logger.info("#doGet");

        response.setContentType("text/html");

        // Hello
        try {
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("</body></html>");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}