package teste;

import domain.Nota;
import org.junit.jupiter.api.Test;
import validator.ValidationException;
import validator.Validator;
import validator.ValidatorNota;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorNotaTest {
    Nota nota1=new Nota("9999","10","12.4","10");
    Nota nota2=new Nota("9999","10","15","10");
    Nota nota3=new Nota("9999","10","12","11");
    Nota nota4=new Nota("9999","10","12","9.15");
    Nota nota5=new Nota("9999","10","12","9");
    Validator<Nota> val=new ValidatorNota();
    @Test
    void validate() {
        try{
            val.validate(nota1);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Data invalida!",e.getMessage());
        }

        try{
            val.validate(nota2);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Data invalida!",e.getMessage());
        }

        try{
            val.validate(nota3);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Nota invalida!",e.getMessage());
        }

        try{
            val.validate(nota4);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Nota invalida!",e.getMessage());
        }

        try {
            val.validate(nota5);
            assertTrue(true);
        }
        catch (ValidationException e){
            fail();
        }
    }
}