package railway.itermit.com.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Initializes locale settings for multi-language resources.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /*
     * Stores locale's settings in ServletContext attribute "locales".
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("Starting application...");

        ServletContext context = event.getServletContext();
        String localesFileName = context.getInitParameter("locales");
        String localesFileRealPath = context.getRealPath(localesFileName);

        if (localesFileRealPath == null) {
            System.out.println("Can't define path for locales - check params in web.xml");
        }

        Properties locales = new Properties();
        try {
            locales.load(new FileInputStream(localesFileRealPath));
//            locales.list(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.setAttribute("locales", locales);

//        ServletContext servletContext = event.getServletContext();
//        String path = servletContext.getRealPath("/WEB-INF/log4j2.log");
//
//        System.out.println("path = " + path);
//
//        System.setProperty("logFile", path);

//        final Logger log = LogManager.getLogger(ContextListener.class);
//        log.debug("path = " + path);


    }

}
