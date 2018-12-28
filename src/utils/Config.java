package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Config {
    private static String usernameAdmin = "proiect.map.teo@gmail.com";
    private static String passwordAdmin = "admin@catalog";
    public static String pathRezultate="./src/data/results/" ;

    public static <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p, Comparator<E> c) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        if(c == null)
            return lista.stream().filter(p).collect(Collectors.toList());
        if(p == null)
            return lista.stream().sorted(c).collect(Collectors.toList());
        return lista.stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    public static Integer getCurrentWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public static Integer getWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public static Integer getWeekUni(Integer saptCurenta){
        Integer dif=saptCurenta-39;
        if(dif<0)
            dif=saptCurenta+13;
        if(dif<1||dif>16) return null;
        if(dif.equals(13) || dif.equals(14))
            return 12;
        if(dif.equals(15)||dif.equals(16)) return dif-2;
        return dif;
    }

    public static Integer getWeekUni(){
        Integer saptCurenta=getCurrentWeek();
        Integer dif=saptCurenta-39;
        if(dif<0)
            dif=saptCurenta+13;
        if(dif<1||dif>16) return null;
        if(dif.equals(13) || dif.equals(14))
            return 12;
        if(dif.equals(15)||dif.equals(16)) return dif-2;
        return dif;
    }

    public static void sendMail(List<String> emails,String subject,String text){
        for(String email:emails){
            sendMail(email,subject,text);
        }
    }

    public static void sendMail(String to, String subject, String text) {

    //    new Thread(()-> {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.prot", "465");

            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(usernameAdmin, passwordAdmin);
                        }
                    }
            );
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(usernameAdmin));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(text);
                Transport.send(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
      //  }).start();
    }

}
