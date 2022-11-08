package com.itermit.railway;


import com.itermit.railway.db.DBManager;
import com.itermit.railway.utils.*;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.mail.Session;
import javax.mail.Transport;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;

/**
 * Utils - Tests.
 * <p>
 * Includes test for: EmailUtils, Paginator, QueryMaker, PropertiesLoader.
 *
 * @author O.Savinov
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerFactory.class, Logger.class,
        PropertiesLoader.class,
        Transport.class,
        DBManager.class,
        Session.class})
public class UtilsTest {

    @Before
    public void setUp() throws IOException {
        PowerMockito.mockStatic(PropertiesLoader.class);
        PowerMockito.mockStatic(Transport.class);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.mockStatic(Logger.class);

        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        properties.setProperty("mail.smtp.host", "mail.smtp.host");
        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);
    }

    /**
     * EmailUtils Test
     *
     * @throws Exception
     */
    @Test
    public final void EmailUtilsTest() throws Exception {

        Logger mockLogger = PowerMockito.mock(Logger.class);

        Whitebox.setInternalState(SendEmailUtil.class, "logger", mockLogger);

        SendEmailUtil.sendEmail("email@mail.com", "test", "test");
    }

    /**
     * Paginator Test
     *
     * @throws Exception
     */
    @Test
    public final void PaginatorTest() throws Exception {

        PowerMockito.mockStatic(PropertiesLoader.class);

        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);

        Paginator paginator = new Paginator.Builder()
                .withPages(11)
                .withPage(2)
                .withResults(3)
                .withData(3)
                .build();
        Paginator.Builder builder = Mockito.mock(Paginator.Builder.class);
        Mockito.when(builder.build()).thenReturn(paginator);

        Assertions.assertEquals(
                "Paginator{page=2, pages=11, prevPage=1, nextPage=3, data=3, results=3}",
                paginator.toString());
        Assertions.assertEquals(false, paginator.pageIsFirst());
        Assertions.assertEquals(false, paginator.pageIsLast());

    }

    /**
     * QueryMaker Test
     *
     * @throws Exception
     */
    @Test
    public final void QueryMakerTest() throws Exception {

        QueryMaker queryMakerMock = PowerMockito.mock(QueryMaker.class);

        QueryMaker queryMaker = new QueryMaker.Builder()
                .withMainQuery("SELECT * FROM users")
                .withCondition("route_id", Condition.EQ, "1")
                .withCondition("seats_available", Condition.EQ, "1")
                .withCondition("travel_time", Condition.EQ, "1")
                .withCondition("train_number", Condition.LIKE, "1")
                .withSort("seats_available", Condition.ASC)
                .withSort("travel_time", Condition.DESC)
                .withPaginator(1)
                .build();

        Assertions.assertEquals("SELECT * FROM users", queryMaker.getQueryMain().toString());
        Assertions.assertEquals(" WHERE route_id = ? AND seats_total - seats_reserved = ? AND TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) = ? AND train_number LIKE ?",
                queryMaker.getQueryCondition().toString());
        Assertions.assertEquals(1, queryMaker.getPage());
        Assertions.assertEquals(" LIMIT 4 OFFSET 0", queryMaker.getQueryOffset().toString());
        Assertions.assertEquals("SELECT * FROM users WHERE route_id = ? AND seats_total - seats_reserved = ? AND TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) = ? AND train_number LIKE ? ORDER BY seats_total - seats_reserved ASC, TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) DESC LIMIT 4 OFFSET 0",
                queryMaker.getFinalQuery());
        Assertions.assertEquals(" ORDER BY seats_total - seats_reserved ASC, TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) DESC", queryMaker.getQuerySort().toString());
        Assertions.assertEquals("QueryMaker{queryMain=SELECT * FROM users WHERE route_id = ? AND seats_total - seats_reserved = ? AND TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) = ? AND train_number LIKE ? ORDER BY seats_total - seats_reserved ASC, TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) DESC LIMIT 4 OFFSET 0, queryCondition= WHERE route_id = ? AND seats_total - seats_reserved = ? AND TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) = ? AND train_number LIKE ?, querySort= ORDER BY seats_total - seats_reserved ASC, TIMESTAMPDIFF(HOUR, routes.date_departure, routes.date_arrival) DESC, queryOffset= LIMIT 4 OFFSET 0}", queryMaker.toString());


        queryMaker.deleteQueryOffset();
        queryMaker.deleteQuerySort();

        Connection connection = PowerMockito.mock(Connection.class);
        PreparedStatement preparedStatement = PowerMockito.mock(PreparedStatement.class);
        PowerMockito.when(queryMakerMock.getPreparedStatement(connection)).thenReturn(preparedStatement);
        Assertions.assertEquals(
                preparedStatement,
                queryMakerMock.getPreparedStatement(connection));

        PowerMockito.when(connection.prepareStatement(any())).thenReturn(preparedStatement);

        Assertions.assertEquals(
                preparedStatement,
                queryMaker.getPreparedStatement(connection));
    }

    /**
     * PropertiesLoader Test
     *
     * @throws Exception
     */
    @Test
    public final void PropertiesLoaderTest() throws Exception {

        Properties properties = new Properties();
        properties.setProperty("pagination.page.size", "4");
        properties.setProperty("mail.smtp.host", "mail.smtp.host");
        Mockito.when(PropertiesLoader.loadProperties()).thenReturn(properties);
    }
}

