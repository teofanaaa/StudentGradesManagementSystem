package validator;

import domain.Nota;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clasa Validator Nota
 * Validarea datelor de intrare pentru note
 */
public class ValidatorNota implements Validator<Nota> {
    private static Pattern datePtrn = Pattern.compile("^[1-9]|1[0-4]$");
    private static Pattern notaPtrn = Pattern.compile("^[1-9]|[1-9][.,][0-9]|10[.]?[0]?$");

    /**
     * Validarea datei
     * @param data - string (un numar de la 1 la 14)
     * @throws ValidationException daca data nu e valida
     */
    private void validateDate(String data){
        Matcher mtch = datePtrn.matcher(data);
        if(!mtch.matches()){
            throw new ValidationException("Data invalida!");
        }
    }

    /**
     * Validarea notei
     * @param notaStr - string (un numar de la 1 la 10)
     * @throws ValidationException daca nota nu e valida
     */
    private void validateNota(String notaStr){
        Matcher mtch = notaPtrn.matcher(notaStr);
        if(!mtch.matches())
            throw new ValidationException("Nota invalida!");
        float nota=Float.parseFloat(notaStr);
        if(nota<1)
            throw new ValidationException("Dati o nota mai mare sau egala cu 1!");
        if(nota>10)
            throw new ValidationException("Dati o nota mai mica sau egala cu 10!");


    }

    /**
     * Functia de validare
     * @param entity - entitatea de validat
     * @throws ValidationException
     */
    @Override
    public void validate(Nota entity) throws ValidationException {
        validateDate(entity.getDataCurenta());
        validateNota(entity.getNotaProf());

    }
}
