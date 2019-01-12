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

    public static String pathRezultate="./src/data/results/" ;
    public static String pathRaport="./src/data/raport" ;

    /**
     * Filtrare si sortare
     * @param lista - list (lista de sortat)
     * @param p - predicate (algoritmul dupa care se sorteaza)
     * @param c - comparator
     * @param <E> - entitatea de stortat/filtrat
     * @return lista sortata/filtrata
     */
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

    /**
     * Saptamana curenta din an
     * @return integer
     */
    public static Integer getCurrentWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     * Saptamana in care se afa o anumita data calendaristica
     * @param date - data calendaristica
     * @return integer
     */
    public static Integer getWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     * Saptamana universitara dintr-o anumita saptamana calendaristica
     * @param saptCurenta - integer (saptamana calendaristica)
     * @return integer
     */
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

    /**
     * Saptamana universitara curenta
     * @return integer
     */
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
}
