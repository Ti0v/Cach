package com.CachWeb.Cach.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String toEmail, String subject, String body) {
// Sender's email ID needs to be mentioned
        String from = "ti0v85@gmail.com";
        // Recipient's email ID needs to be mentioned
        String to = toEmail;
        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        // Get the default Session object.
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ti0v85@gmail.com", "irkmamrjnhpfalbp");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject(subject);
            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
//    @Async
//    public void sendEmail(String toEmail, String subject, String body) {
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setFrom("ti0v85@gmail.com");
//            mailMessage.setTo(toEmail);
//            mailMessage.setSubject(subject);
//            mailMessage.setText(body);
//
//            mailSender.send(mailMessage);
////            System.out.println("Email sent successfully");
//        } catch (Exception e) {
//            e.printStackTrace();  // Handle exceptions appropriately
////            System.out.println("The error"+ e.getMessage());
//        }
//    }


}
