package teste;

import domain.Utilizator;
import org.junit.jupiter.api.Test;
import validator.ValidationException;
import validator.Validator;
import validator.ValidatorUtilizator;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorUtilizatorTest {
    Validator<Utilizator> val=new ValidatorUtilizator();
    Utilizator user1=new Utilizator("etir2!22", "passs", Utilizator.TipUtilizator.valueOf("STUDENT"), "Teofana Enachioiu");
    Utilizator user3=new Utilizator("2222", "passs", Utilizator.TipUtilizator.valueOf("STUDENT"), "Teofana En4chioiu");
    Utilizator user4=new Utilizator("2222", "passs", Utilizator.TipUtilizator.valueOf("STUDENT"), "Teofana Enachioiu");
    @Test
    void validateTest(){
        try{
            val.validate(user1);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Utilizator incorect!", e.getMessage());
        }

        try{
            val.validate(user3);
            fail();
        }
        catch (ValidationException e){
            assertEquals("Nume incorect!",e.getMessage());
        }

        try{
            val.validate(user4);
            assertTrue(true);
        }
        catch (ValidationException e){
            fail();
        }
    }

}