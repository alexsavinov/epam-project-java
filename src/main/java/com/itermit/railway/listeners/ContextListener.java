package com.itermit.railway.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 *  Initializes locale settings for multi-language resources.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    private static final String LOCALES = "locales";
    private static final String LOCALES_ERROR = "Can't define path for locales - check params in web.xml";
    private static final String PROPERTIES_ERROR = "Can't define path for properties - check if app.properties exists.";

    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("Starting application...");

        /* Init path for logs */
        ServletContext context = event.getServletContext();
        String logPath = context.getRealPath("/logs");
        System.setProperty("logPath", logPath);

        Logger logger = null;

        try {
            logger = LogManager.getLogger(ContextListener.class);
        } catch (RuntimeException exception) {
            System.out.println("Cannot start Logger!");
            System.out.println("logPath: " + logPath);
            exception.printStackTrace();
        }

        /* Init locales settings */
        loadLocales(context);

        /* Reading settings from properties file  */
//        getProperties(context);

        if (logger != null) {
            logger.warn("locales.list = {}", context.getAttribute(LOCALES));
            logger.warn("logPath = {}", logPath);
        }
    }

    /*
     *  Init locales settings
     */
    private void loadLocales(ServletContext context) {

        String localesFileName = context.getInitParameter(LOCALES);
        String localesFileRealPath = context.getRealPath(localesFileName);

        if (localesFileRealPath == null) {
            System.out.println(LOCALES_ERROR);
            return;
        }

        Properties locales = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(localesFileRealPath)) {
            locales.load(fileInputStream);
            context.setAttribute(LOCALES, locales);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + localesFileRealPath);
        } catch (IOException e) {
            System.err.println("ERROR loadLocales: " + e.getMessage());
        }

    }

    /*
     *  Reading settings from properties file
     */
    private void getProperties(ServletContext context) {

        String propsFileName = context.getInitParameter("app.properties");
        String propsFileRealPath = context.getRealPath(propsFileName);

        if (propsFileRealPath == null) {
            System.out.println(PROPERTIES_ERROR);
            return;
        }

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(propsFileRealPath)) {
            properties.load(fileInputStream);

            context.setAttribute("daofqn", properties.getProperty("dao.fqn"));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + propsFileRealPath);
        } catch (IOException e) {
            System.err.println(PROPERTIES_ERROR + " " + e.getMessage());
        }
    }

}
