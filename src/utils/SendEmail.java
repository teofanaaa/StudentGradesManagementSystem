package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Clasa SendEmail
 * Trimitere de mailuri concurent
 */
public class SendEmail extends Thread{
    private String to;
    private String mesaj;
    private String subject;

    /**
     * Constructorul clasei
     * @param to - string (emailul destinatarului)
     * @param message - string (mesajul de trimis)
     * @param subject - string (subiectul mesajului)
     */
    public SendEmail(String to,String message,String subject)
    {
        this.to=to;
        this.mesaj=message;
        this.subject=subject;
    }

    /**
     * Trimitearea de mail-uri
     */
    @Override
    public void run()
    {
        new Thread(()-> {
            // Sender's email ID needs to be mentioned
            String from = "proiect.map.teo@gmail.com";

            // Assuming you are sending email from localhost
            String host = "smtp.gmail.com";

            // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.setProperty("mail.smtp.host", host);
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");

            // Get the default Session object.
            Session session = Session.getDefaultInstance(properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication("proiect.map.teo@gmail.com", "admin@catalog");
                        }
                    }
            );
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
                message.setText(mesaj);

                // Send message
                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }

        }).start();
    }

}