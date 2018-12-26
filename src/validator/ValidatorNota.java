package validator;

import domain.Nota;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorNota implements Validator<Nota> {
    private static Pattern datePtrn = Pattern.compile("^[1-9]|1[0-4]$");
    private static Pattern notaPtrn = Pattern.compile("^[1-9]|[1-9][.,][0-9]|10[.]?[0]?$");

    private void validateDate(String data){
        Matcher mtch = datePtrn.matcher(data);
        if(!mtch.matches()){
            throw new ValidationException("Data invalida!");
        }
    }

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
    @Override
    public void validate(Nota entity) throws ValidationException {
        validateDate(entity.getDataCurenta());
        validateNota(entity.getNotaProf());

    }
}
