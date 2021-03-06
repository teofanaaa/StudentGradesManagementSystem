package validator;

import domain.Utilizator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clasa Validator Utlizator
 * Validarea datelor de intrare pentru utilizator
 */
public class ValidatorUtilizator implements Validator<Utilizator> {
    private static Pattern usrPtrn = Pattern.compile("^[A-Za-z0-9_.]+$");
    private static Pattern usrNamePtrn = Pattern.compile("^[A-Za-z ,.'-]+$");

    /**
     * Functia de validare. Se vaideaza id-ul utilizatorului si numele sau
     * @param entity - entitatea de validat
     * @throws ValidationException
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        Matcher mtch = usrPtrn.matcher(entity.getID());
        if(!mtch.matches()){
            throw new ValidationException("Utilizator incorect!");
        }

        mtch=usrNamePtrn.matcher(entity.getNume());
        if(!mtch.matches()){
            throw new ValidationException("Nume incorect!");
        }
    }
}
