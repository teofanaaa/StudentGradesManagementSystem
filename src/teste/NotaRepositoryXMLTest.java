package teste;

import domain.Nota;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import repository.xml.NotaRepositoryXML;
import repository.Repository;
import validator.ValidatorNota;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotaRepositoryXMLTest {
    private Repository<Pair<String, String>, Nota> repo=new NotaRepositoryXML("./src/teste/Catalog_test.xml", new ValidatorNota());

    @Test
    void findAll() {
        int size=0;
        for(Nota n:repo.findAll()) size++;
        assertEquals(2,size);
    }

    @Test
    void findOne() {
        try{
            repo.findOne(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        assertEquals(Optional.empty(),repo.findOne(Optional.of(new Pair("1111","4"))));
        Nota nota=new Nota("1001","4","10","10");
        repo.save(Optional.of(nota));
        assertEquals(Optional.of(nota),repo.findOne(Optional.of(new Pair("1001","4"))));
    }

    @Test
    void save() {
        try{
            repo.save(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        Nota nota=new Nota("1001","2","10","10");
        assertEquals(Optional.empty(),repo.save(Optional.of(nota)));
        repo.delete(Optional.of(new Pair("1001","2")));
        Nota nota1=new Nota("1001","4","10","10");
        assertEquals(Optional.of(nota1),repo.save(Optional.of(nota1)));

    }

    @Test
    void delete() {
        try{
            repo.delete(Optional.empty());
        }
        catch (IllegalArgumentException e){
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        assertEquals(Optional.empty(),repo.delete(Optional.of(new Pair("1001","2"))));

        Nota nota=new Nota("1001","2","10","10");
        repo.save(Optional.of(nota));

        assertEquals(Optional.of(nota),repo.delete(Optional.of(new Pair("1001","2"))));
    }

    @Test
    void update() {
        try {
            repo.update(Optional.empty());
        } catch (IllegalArgumentException e) {
            assertEquals("Nu ai dat parametru", e.getMessage());
        }
        Nota nota = new Nota("1001", "4", "10", "9");
        Nota nota1 = new Nota("1001", "4", "10", "10");
        Nota nota2 = new Nota("1010", "4", "10", "10");
        assertEquals(Optional.empty(), repo.update(Optional.of(nota)));
        assertEquals(Optional.empty(), repo.update(Optional.of(nota1)));
        assertEquals(Optional.of(nota2), repo.update(Optional.of(nota2)));
    }

}