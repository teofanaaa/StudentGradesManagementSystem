package teste;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import repository.Repository;
import repository.database.DatabaseCreator;
import repository.database.NotaRepositoryDatabase;
import repository.database.StudentRepositoryDatabase;
import repository.database.TemaRepositoryDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotaRepositoryDatabaseTest {
    private DatabaseCreator creator=new DatabaseCreator("/src/teste/Test_DB_1");
    private Repository<String,Student> repoStudenti=new StudentRepositoryDatabase(creator);
    private Repository<String, Tema> repoTeme=new TemaRepositoryDatabase(creator);
    private Repository<Pair<String, String>, Nota> repo=new NotaRepositoryDatabase(creator);

    private Student student=new Student("1001","Enachioiu", "Teofana",
            "223","teo@yahoo.com","Guran A");
    private Tema tema1= new Tema("4","Lab 4", "5","4");
    private Tema tema2= new Tema("5","Lab 5", "6","5");

    private Nota nota1=new Nota("1001","4","10","10");
    private Nota nota2=new Nota("1001","5","10","10");


    @Test
    void findAll() {
        creator.execAction("DELETE FROM " + "STUDENTI");
        creator.execAction("DELETE FROM " + "TEME");
        creator.execAction("DELETE FROM " + "NOTE");

        int size=0;
        repoStudenti.save(Optional.of(student));
        for(Student s:repoStudenti.findAll()) size++;

        size=0;
        repoTeme.save(Optional.of(tema1));
        repoTeme.save(Optional.of(tema2));
        for(Tema t:repoTeme.findAll()) size++;

        repo.save(Optional.of(nota1));
        repo.save(Optional.of(nota2));


        size=0;
        for(Nota n:repo.findAll()) size++;
        assertEquals(2,size);
    }

    @Test
    void findOne() {
        creator.execAction("DELETE FROM " + "STUDENTI");
        creator.execAction("DELETE FROM " + "TEME");
        creator.execAction("DELETE FROM " + "NOTE");

        repoStudenti.save(Optional.of(student));

        repoTeme.save(Optional.of(tema1));
        repoTeme.save(Optional.of(tema2));

        try{
            repo.findOne(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        assertEquals(Optional.empty(),repo.findOne(Optional.of(new Pair("1111","4"))));
        //Nota nota=new Nota("1001","4","10","10");
        repo.save(Optional.of(nota1));
        assertEquals(Optional.of(nota1),repo.findOne(Optional.of(new Pair("1001","4"))));
    }

    @Test
    void save() {
        creator.execAction("DELETE FROM " + "STUDENTI");
        creator.execAction("DELETE FROM " + "TEME");
        creator.execAction("DELETE FROM " + "NOTE");

        repoStudenti.save(Optional.of(student));

        repoTeme.save(Optional.of(tema1));
        repoTeme.save(Optional.of(tema2));

        try{
            repo.save(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }

        assertEquals(Optional.empty(),repo.save(Optional.of(nota1)));
        assertEquals(Optional.of(nota1),repo.save(Optional.of(nota1)));
        repo.delete(Optional.of(new Pair("1001","4")));


    }

    @Test
    void delete() {
        creator.execAction("DELETE FROM " + "STUDENTI");
        creator.execAction("DELETE FROM " + "TEME");
        creator.execAction("DELETE FROM " + "NOTE");

        repoStudenti.save(Optional.of(student));

        repoTeme.save(Optional.of(tema1));
        repoTeme.save(Optional.of(tema2));

        try{
            repo.delete(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        assertEquals(Optional.empty(),repo.delete(Optional.of(new Pair("1001","2"))));

        //Nota nota=new Nota("1001","2","10","10");
        repo.save(Optional.of(nota1));

        assertEquals(Optional.of(nota1),repo.delete(Optional.of(new Pair("1001","4"))));
    }

    @Test
    void update() {
        creator.execAction("DELETE FROM " + "STUDENTI");
        creator.execAction("DELETE FROM " + "TEME");
        creator.execAction("DELETE FROM " + "NOTE");

        repoStudenti.save(Optional.of(student));

        repoTeme.save(Optional.of(tema1));
        repoTeme.save(Optional.of(tema2));

        try{
            repo.update(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
//        Nota nota=new Nota("1001","4","10","9");
//        Nota nota1=new Nota("1001","4","10","10");
//        Nota nota2=new Nota("1010","4","10","10");
        assertEquals(Optional.of(nota1),repo.update(Optional.of(nota1)));
        repo.save(Optional.of(nota1));
        assertEquals(Optional.empty(),repo.update(Optional.of(nota1)));
        //assertEquals(Optional.of(nota2),repo.update(Optional.of(nota2)));
    }


}