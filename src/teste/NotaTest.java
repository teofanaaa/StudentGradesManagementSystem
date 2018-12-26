package teste;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotaTest {


    private Nota nota = new Nota( "1001", "1", "2","10");

    @Test
    void getStudent() {
        assertEquals("1001",nota.getStudentID());
    }

    @Test
    void setStudent() {
        nota.setStudentID("2222");
        assertEquals("2222",nota.getStudentID());
    }

    @Test
    void getTema() {
        assertEquals("1",nota.getTemaID());
    }

    @Test
    void setTema() {
        nota.setTemaID("2");
        assertEquals("2",nota.getTemaID());
    }

    @Test
    void getDataCurenta() {
        assertEquals("2",nota.getDataCurenta());
    }

    @Test
    void setDataCurenta() {
        nota.setDataCurenta("3");
        assertEquals("3",nota.getDataCurenta());
    }

    @Test
    void getNotaProf() {
        assertEquals("10",nota.getNotaProf());
    }

    @Test
    void setNotaProf() {
        nota.setNotaProf("9");
        assertEquals("9",nota.getNotaProf());
    }

    @Test
    void getID() {
        assertEquals(new Pair("1001","1"),nota.getID());
    }

    @Test
    void setID() {
        nota.setID(new Pair("2222","2"));
        assertEquals(new Pair("2222","2"), nota.getID());
    }

    @Test
    void equals() {
        Nota n = new Nota( "1001", "1", "2","10");
        assertEquals(n,nota);
        n.setNotaProf("9");
        assertNotEquals(n,nota);
    }

    @Test
    void toStringTest() {
        assertEquals("1001/1/2/10",nota.toString());
    }
}