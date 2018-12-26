package teste;

import domain.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student=new Student("2200","Enachioiu",
            "Teofana","223","teo@yahoo.com","Guran A");

    @Test
    void getNume() {
        assertEquals("Enachioiu",student.getNume());
    }

    @Test
    void setNume() {
        student.setNume("Amariei");
        assertEquals("Amariei",student.getNume());
    }

    @Test
    void getPrenume() {
        assertEquals("Teofana",student.getPrenume());
    }

    @Test
    void setPrenume() {
        student.setPrenume("Teona");
        assertEquals("Teona",student.getPrenume());
    }

    @Test
    void getGrupa() {
        assertEquals("223",student.getGrupa());
    }

    @Test
    void setGrupa() {
        student.setGrupa("233");
        assertEquals("233",student.getGrupa());
    }

    @Test
    void getEmail() {
        assertEquals("teo@yahoo.com",student.getEmail());
    }

    @Test
    void setEmail() {
        student.setEmail("teofana@yahoo.com");
        assertEquals("teofana@yahoo.com",student.getEmail());
    }

    @Test
    void getIndrumatorLab() {
        assertEquals("Guran A",student.getIndrumatorLab());
    }

    @Test
    void setIndrumatorLab() {
        student.setIndrumatorLab("Guran Adriana");
        assertEquals("Guran Adriana",student.getIndrumatorLab());
    }

    @Test
    void getID() {
        assertEquals("2200",student.getID());
    }

    @Test
    void setID() {
        student.setID("2222");
        assertEquals("2222",student.getID());
    }

    @Test
    void toStringTest() {
        assertEquals("2200/Enachioiu/Teofana/223/teo@yahoo.com/Guran A",student.toString());
    }

    @Test
    void equals() {
        Student s1=new Student("2200","Enachioiu",
                "Teofana","223","teo@yahoo.com","Guran A");
        Student s2=new Student("2200","Enachioiu",
                "Teofana","223","teo@yahoo.com","Guran A");
        assertEquals(s1, s2);
        s1.setID("2222");
        assertNotEquals(s1, s2);
    }
}