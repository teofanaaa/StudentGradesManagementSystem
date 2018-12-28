package teste;

import domain.Profesor;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.database.DatabaseCreator;
import repository.database.ProfesorRepositoryDatabase;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class ProfesorRepositoryDatabaseTest {
    Repository<String, Profesor> repo=new ProfesorRepositoryDatabase(new DatabaseCreator("/src/teste/Test_DB"));
    Profesor prof1=new Profesor("1","Guran A","adriana@yahoo.com");
    Profesor prof2=new Profesor("2","Serban C","camelia@yahoo.com");

    @Test
    void findAllTest(){
        assertNull( repo.findAll());
        repo.save(Optional.of(prof1));
        repo.save(Optional.of(prof2));
        assertEquals(2, StreamSupport.stream(repo.findAll().spliterator(),false).count());
        repo.delete(Optional.of("1"));
        repo.delete(Optional.of("2"));
    }

}