package teste;

import domain.Tema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemaTest {

    private Tema tema=new Tema("1","Laborator 1","2","1");

    @Test
    void getDescriere() {
        assertEquals("Laborator 1",tema.getDescriere());
    }

    @Test
    void setDescriere() {
        tema.setDescriere("Lab 1");
        assertEquals("Lab 1",tema.getDescriere());
    }

    @Test
    void getDeadline() {
        assertEquals("2",tema.getDeadline());
    }

    @Test
    void setDeadline() {
        tema.setDeadline("3");
        assertEquals("3",tema.getDeadline());
    }

    @Test
    void getDataPredare() {
        assertEquals("1",tema.getDataPredare());
    }

    @Test
    void setDataPredare() {
        tema.setDataPredare("2");
        assertEquals("2",tema.getDataPredare());
    }

    @Test
    void getID() {
        assertEquals("1",tema.getID());
    }

    @Test
    void setID() {
        tema.setID("2");
        assertEquals("2",tema.getID());
    }

    @Test
    void equals() {
        Tema t=new Tema("1","Laborator 1","2","1");
        assertEquals(t,tema);
        t.setDataPredare("2");
        assertNotEquals(t,tema);
    }

    @Test
    void toStringTest() {
        assertEquals("1/Laborator 1/2/1",tema.toString());
    }
}