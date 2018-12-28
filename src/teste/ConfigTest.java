package teste;

import org.junit.jupiter.api.Test;
import utils.Config;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void sendMailTest() {
        Config.sendMail("teofanaenachioiu@yahoo.com","Test from Java app","Am reusit!");
    }

    @Test
    void currentWeekUniTest(){
        assertNull(Config.getWeekUni(Config.getWeek(LocalDate.of(2018, 9, 27))));
        assertEquals(Optional.of(11), java.util.Optional.ofNullable(Config.getWeekUni(Config.getWeek(LocalDate.of(2018, 12, 12)))));
        assertEquals(Optional.of(12), java.util.Optional.ofNullable(Config.getWeekUni(Config.getWeek(LocalDate.of(2018, 12, 19)))));
        assertEquals(Optional.of(12), java.util.Optional.ofNullable(Config.getWeekUni(Config.getWeek(LocalDate.of(2018, 12, 27)))));
        assertEquals(Optional.of(12), java.util.Optional.ofNullable(Config.getWeekUni(Config.getWeek(LocalDate.of(2019, 1, 1)))));
        assertEquals(Optional.of(13), java.util.Optional.ofNullable(Config.getWeekUni(Config.getWeek(LocalDate.of(2019, 1, 7)))));
        assertEquals(Optional.of(14), java.util.Optional.ofNullable(Config.getWeekUni(Config.getWeek(LocalDate.of(2019, 1, 14)))));
        assertNull(Config.getWeekUni(Config.getWeek(LocalDate.of(2019, 1, 27))));
    }
}