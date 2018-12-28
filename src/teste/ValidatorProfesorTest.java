package teste;

import domain.Profesor;
import org.junit.jupiter.api.Test;
import validator.ValidationException;
import validator.Validator;
import validator.ValidatorProfesor;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorProfesorTest {
    Validator<Profesor> val=new ValidatorProfesor();
    Profesor prof1=new Profesor("02","Guran A","adriana@yahoo.com");
    Profesor prof2=new Profesor("1","Guran A4","adriana@yahoo.com");
    Profesor prof3=new Profesor("1","Guran A","adriana@yahoocom");
    Profesor prof4=new Profesor("1","Guran A","adriana@yahoo.com");

    @Test
    void validatateTest(){
        try{
            val.validate(prof1);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Id incorect!",e.getMessage());
        }

        try{
            val.validate(prof2);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Nume incorect!",e.getMessage());
        }

        try{
            val.validate(prof3);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Email incorect!",e.getMessage());
        }

        try{
            val.validate(prof4);
            assertTrue(true);
        }
        catch (ValidationException e){
            fail();
        }
    }

}