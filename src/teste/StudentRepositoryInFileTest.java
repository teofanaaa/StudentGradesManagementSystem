package teste;

import domain.Student;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.file.StudentRepositoryInFile;
import validator.ValidatorStudent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryInFileTest {
    private Repository<String, Student> repo=new StudentRepositoryInFile("./src/teste/Studenti_test.txt", new ValidatorStudent());
    private Student s1=new Student("9999","Pop","Claudia","223","clau@yahoo.com","1");
    private Student s2=new Student("1001","Pop","Elena","223","ela@yahoo.com","1");

    @Test
    void save() {
        try{
            repo.save(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.delete(Optional.of("9999"));
        assertEquals(Optional.empty(),repo.save(Optional.of(s1)));
        repo.delete(Optional.of("9999"));
        assertEquals(Optional.of(s2),repo.save(Optional.of(s2)));

    }

    @Test
    void findOne() {
        try{
            repo.findOne(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        assertEquals(Optional.empty(),repo.findOne(Optional.of("1010")));
        repo.save(Optional.of(s1));
        assertEquals(Optional.of(s1),repo.findOne(Optional.of("9999")));
    }

    @Test
    void findAll() {
        int size=0;
        for(Student s:repo.findAll()) size++;
        assertEquals(5,size);
    }

    @Test
    void delete() {
        try{
            repo.delete(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.save(Optional.of(s1));
        assertEquals(Optional.empty(),repo.delete(Optional.of("5")));
        assertEquals(Optional.of(s1),repo.delete(Optional.of("9999")));
    }

    @Test
    void update() {
        try{
            repo.update(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.save(Optional.of(s1));
        assertEquals(Optional.empty(),repo.update(Optional.of(s1)));
        Student s3=new Student("1011","Pop","Elena","223","ela@yahoo.com","1");

        assertEquals(Optional.of(s3),repo.update(Optional.of(s3)));
    }
}