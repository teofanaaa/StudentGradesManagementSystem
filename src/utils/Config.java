package utils;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Config {

    public static Integer getCurrentWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public Integer getLabNumber(){
        Integer dif=getCurrentWeek()-39;
        if(dif<1||dif>16) return null;
        if(getCurrentWeek().equals(13) || getCurrentWeek().equals(14))
            return 12;
        if(getCurrentWeek().equals(15)) return dif-1;
        if(getCurrentWeek().equals(16)) return dif-2;
        return dif;
    }

    public Integer getWeek(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    public Integer getWeekUni(Integer saptCurenta){
        Integer dif=saptCurenta-39;
        if(dif<1||dif>16) return null;
        if(saptCurenta.equals(13) || saptCurenta.equals(14))
            return 12;
        if(saptCurenta.equals(15)) return dif-1;
        if(saptCurenta.equals(16)) return dif-2;
        return dif;
    }

}
