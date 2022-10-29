package com.itermit.railway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties loader class.
 *
 * @author O.Savinov
 */
public class PropertiesLoader {

    /**
     * Loads settings from properties file.
     *
     * @return Properties
     * @throws IOException
     */
    public static Properties loadProperties() throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream("app.properties");
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }
}