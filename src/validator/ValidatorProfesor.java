package validator;

import domain.Profesor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clasa Validator Profesor
 * Validarea datelor de intrare pentru profesor
 */
public class ValidatorProfesor implements Validator<Profesor>{
    private static Pattern usrNamePtrn = Pattern.compile("^[A-Za-z ,.'-]+$");
    private static Pattern usrEmailPtrn = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    private static Pattern usrIdPtrn = Pattern.compile("^[1-9]{1,}[0-9]*$");

    /**
     * Validarea id-ului profesorului
     * @param id - numar intreg >0
     * @throws ValidationException daca id-ul nu e valid
     */
    private static void validateID(String id){
        Matcher mtch = usrIdPtrn.matcher(id);
        if(!mtch.matches()){
            throw new ValidationException("Id incorect!");
        }
    }

    /**
     * Validarea numelui profesorului
     * @param name - string (contine litere mari/mici si caracterele speciale: ,.'-)
     * @throws ValidationException daca numele nu e valid
     */
    private static void validateName(String name) {
        Matcher mtch = usrNamePtrn.matcher(name);
        if(!mtch.matches()){
            throw new ValidationException("Nume incorect!");
        }
    }

    /**
     * Validarea email-ului profesorului
     * @param email - string
     * @throws ValidationException daca email-ul nu e valid
     */
    private static void validateEmail(String email)  {
        Matcher mtch = usrEmailPtrn.matcher(email);
        if(!mtch.matches()){
            throw new ValidationException("Email incorect!");
        }
    }

    /**
     * Functia de validare
     * @param entity - entitatea de validat
     * @throws ValidationException
     */
    @Override
    public void validate(Profesor entity) throws ValidationException {
        validateID(entity.getID());
        validateName(entity.getNume());
        validateEmail(entity.getEmail());
    }
}
