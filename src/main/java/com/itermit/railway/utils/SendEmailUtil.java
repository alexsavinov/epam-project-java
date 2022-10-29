package com.itermit.railway.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Sends email message.
 * <p>
 * Reads settings for delivering from properties file.
 *
 * @author O.Savinov
 */
public class SendEmailUtil {

    private static Properties conf;

    static {
        try {
            conf = PropertiesLoader.loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String MAIL_FROM = String.valueOf(conf.getProperty("mail.from"));
    private static String MAIL_PASSWORD = String.valueOf(conf.getProperty("mail.password"));
    private static String MAIL_HOST = String.valueOf(conf.getProperty("mail.smtp.host"));
    private static String MAIL_PORT = String.valueOf(conf.getProperty("mail.smtp.port"));
    private static String MAIL_AUTH = String.valueOf(conf.getProperty("mail.smtp.auth"));
    private static String MAIL_SSL_TRUST = String.valueOf(conf.getProperty("mail.smtp.ssl.trust"));
    private static String MAIL_START_TSL = String.valueOf(conf.getProperty("mail.smtp.starttls.enable"));
    private static String MAIL_SSL_PROTO = String.valueOf(conf.getProperty("mail.smtp.ssl.protocols"));
    private static final Logger logger = LogManager.getLogger(SendEmailUtil.class);

    private SendEmailUtil() {
    }

    /**
     * Sends email message to Recipient.
     *
     * @param recipientEmail String with recipient email address
     * @param subject        String with subject
     * @param content        String with message
     * @throws MessagingException
     */
    public static void sendEmail(String recipientEmail, String subject, String content) throws MessagingException {

        logger.debug("sendEmail(recipientEmail, subject, content): {} {}", recipientEmail, subject);

        Properties properties = new Properties();

        properties.put("mail.smtp.host", MAIL_HOST);
        properties.put("mail.smtp.port", MAIL_PORT);
        properties.put("mail.smtp.auth", MAIL_AUTH);
        properties.put("mail.smtp.ssl.trust", MAIL_SSL_TRUST);
        properties.setProperty("mail.smtp.starttls.enable", MAIL_START_TSL);
        properties.setProperty("mail.smtp.ssl.protocols", MAIL_SSL_PROTO);

        Session session = Session.getDefaultInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MAIL_FROM, MAIL_PASSWORD);
                    }
                });

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(MAIL_FROM, "admin@railway.itermit.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=utf-8");
            message.saveChanges();
            Transport.send(message);


        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
