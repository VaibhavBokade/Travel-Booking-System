package com.example.Travel.Booking.System.mailSending;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Properties;

public class Notification {

    public static void sendRegistrationEmail(String firstName, String lastName, String toEmail) {
        String message = "Hi " + firstName + " " + lastName + " !" + "\n \n You have successfully registered on Travel Booking System ! "; // Your email message content
        String subject = "Registration Confirmation";
        String from = "omkarandhare2317@gmail.com";
        String host = "smtp.gmail.com";
        String username = "omkarandhare2317@gmail.com";
        String password = "jabuuapizyoolffw";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(from));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            m.setSubject(subject);
            m.setText(message);
            Transport.send(m);
            System.out.println("Registration email sent successfully to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBookingEmail(String firstName, String lastName, String toEmail, String source, String destination, double price, LocalDate bookingDate) {
        String message = "Hi " + firstName + " " + lastName + " !" + " \n  You have book your tour from " + source + " \n to " + destination + " with booking price " + price + " on " + bookingDate + " this day...!!! " + " \n\n\n Happy Journey !";
        String subject = "Registration Confirmation";
        String from = "omkarandhare2317@gmail.com";
        String host = "smtp.gmail.com";
        String username = "omkarandhare2317@gmail.com";
        String password = "jabuuapizyoolffw";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(from));
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            m.setSubject(subject);
            m.setText(message);
            Transport.send(m);
            System.out.println("Registration email sent successfully to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
