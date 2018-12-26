package teste;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.file.NotaRepositoryInFile;
import repository.file.StudentRepositoryInFile;
import repository.file.TemaRepositoryInFile;
import service.Service;
import service.StudentService;
import validator.ValidationException;
import validator.ValidatorNota;
import validator.ValidatorStudent;
import validator.ValidatorTema;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    Repository<String, Student> repoS=new StudentRepositoryInFile("./src/teste/Studenti_test.txt", new ValidatorStudent());
    Repository<String, Tema> repoT=new TemaRepositoryInFile("./src/teste/Teme_test.txt",new ValidatorTema());
    Repository<Pair<String,String>, Nota> repoN=new NotaRepositoryInFile("./src/teste/Catalog_test.txt",new ValidatorNota());

    StudentService service = new StudentService(repoS,repoT,repoN);

    @Test
    void adaugaTest(){
        Student student1=new Student("1001","","Teofana","223","teo@yahoo.com","Guran A");
        try{
            service.add(student1);
            fail();
        }
        catch (ValidationException e){
            assertTrue(true);
        }
        Student student2=new Student("9999","Enachioiu","Teofana","223","teo@yahoo.com","Guran A");
        assertNull(service.add(student2));
        assertEquals(student2,service.add(student2));
        service.remove("9999");
    }

    @Test
    void stergeTest(){
        Student student=new Student("9999","Enachioiu","Teofana","223","teo@yahoo.com","Guran A");

        assertNull(service.remove("9999"));
        assertNull(service.remove("aad5"));
        service.add(student);
        assertEquals(student,service.remove("9999"));
    }

    @Test
    void actualizareTest(){
        Student student=new Student("9999","Enachioiu","Teofana","223","teo@yahoo.com","Guran A");
        Student studentUpd=new Student("9999","","Teo","","teofana@yahoo.com","");
        assertNull(service.add(student));
        student.setPrenume("Teo");
        student.setEmail("teofana@yahoo.com");
        assertNull(service.update(studentUpd));
        assertEquals(student,service.find("9999"));
        service.remove("9999");
        assertEquals(student,service.update(student));
    }

    @Test
    void sizeTest(){
        assertEquals(5,(int)service.size());
    }

    @Test
    void cautaTest(){
        Student student=new Student("9999","Enachioiu","Teofana","223","teo@yahoo.com","Guran A");
        assertNull(service.find("9999"));
        service.add(student);
        assertEquals(student,service.find("9999"));
        service.remove("9999");
    }

    @Test
    void getGrupeTest(){
        List<String> rez=service.getGrupe();
        assertEquals(5,rez.size());
        assertEquals("221",rez.get(0));
        assertEquals("223",rez.get(1));
        assertEquals("224",rez.get(2));
        assertEquals("226",rez.get(3));
        assertEquals("227",rez.get(4));
    }

    @Test
    void getGrupeProfTest(){
        List<String> rez=service.getGrupeProf("Guran A");
        assertEquals(1,rez.size());
        assertEquals("223",rez.get(0));
    }


}