package com.itermit.railway.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class SendEmailUtil {

    private static final Logger logger = LogManager.getLogger(SendEmailUtil.class);

    private SendEmailUtil() {
    }

//    public static void sendEmail(String recipientEmail, String subject, String content) throws MessagingException {
//
//        logger.info(" >>>>>>>>>>>> recipientEmail: {}", recipientEmail);
//
//        String fromEmail = "spell477.temp@gmail.com";
//        String password = "raeubnmkwqesebwp";
//
//        Properties properties = new Properties();
//
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.auth", "true");
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

//        Session session = Session.getDefaultInstance(
//                properties,
//                new Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(fromEmail, password);
//                    }
//                });
//
//        logger.info("properties: {}", properties);
//
//        MimeMessage message = new MimeMessage(session);
//        try {
//            message.setFrom(new InternetAddress(fromEmail, "Elective"));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
//            message.setSubject(subject);
//            message.setContent(content, "text/html;charset=utf-8");
//            message.saveChanges();
//            Transport.send(message);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }

//    }

}
