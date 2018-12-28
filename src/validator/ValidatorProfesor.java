package validator;

import domain.Profesor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorProfesor implements Validator<Profesor>{
    private static Pattern usrNamePtrn = Pattern.compile("^[A-Za-z ,.'-]+$");
    private static Pattern usrEmailPtrn = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    private static Pattern usrIdPtrn = Pattern.compile("^[1-9]{1,}[0-9]*$");

    private static void validateID(String id){
        Matcher mtch = usrIdPtrn.matcher(id);
        if(!mtch.matches()){
            throw new ValidationException("Id incorect!");
        }
    }

    private static void validateName(String name) {
        Matcher mtch = usrNamePtrn.matcher(name);
        if(!mtch.matches()){
            throw new ValidationException("Nume incorect!");
        }
    }

    private static void validateEmail(String email)  {
        Matcher mtch = usrEmailPtrn.matcher(email);
        if(!mtch.matches()){
            throw new ValidationException("Email incorect!");
        }
    }

    @Override
    public void validate(Profesor entity) throws ValidationException {
        validateID(entity.getID());
        validateName(entity.getNume());
        validateEmail(entity.getEmail());
    }
}
