package teste;

import domain.Tema;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.database.DatabaseCreator;
import repository.database.TemaRepositoryDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TemaRepositoryDatabaseTest {

    private DatabaseCreator creator=new DatabaseCreator("/src/teste/Test_DB");

    private Repository<String, Tema> repo=new TemaRepositoryDatabase( new DatabaseCreator("/src/teste/Test_DB"));
    private Tema tema1=new Tema("1","Lab 1","2","1");
    private Tema tema2=new Tema("2","Lab 2","3","2");

    @Test
    void save() {
        try{
            repo.save(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.delete(Optional.of("1"));
        assertEquals(Optional.empty(),repo.save(Optional.of(tema1)));
        assertEquals(Optional.of(tema1),repo.save(Optional.of(tema1)));
        repo.delete(Optional.of("1"));

    }

    @Test
    void findOne() {
        try{
            repo.findOne(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.delete(Optional.of("5"));
        assertEquals(Optional.empty(),repo.findOne(Optional.of("5")));
        repo.save(Optional.of(tema1));
        assertEquals(Optional.of(tema1),repo.findOne(Optional.of("1")));
    }

    @Test
    void findAll() {
        for(Tema t:repo.findAll()){
            repo.delete(Optional.ofNullable(t.getID()));
        }
        int size=0;
        repo.save(Optional.of(tema1));
        repo.save(Optional.of(tema2));
        for(Tema t:repo.findAll()) size++;
        assertEquals(2,size);
    }

    @Test
    void delete() {
        try{
            repo.delete(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.save(Optional.of(tema1));
        assertEquals(Optional.empty(),repo.delete(Optional.of("5")));
        assertEquals(Optional.of(tema1),repo.delete(Optional.of("1")));
    }

    @Test
    void update() {
        for(Tema t:repo.findAll()){
            repo.delete(Optional.of(t.getID()));
        }
        try{
            repo.update(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        repo.save(Optional.of(tema1));
        assertEquals(Optional.empty(),repo.update(Optional.of(tema1)));

        assertEquals(Optional.of(tema2),repo.update(Optional.of(tema2)));
    }
}