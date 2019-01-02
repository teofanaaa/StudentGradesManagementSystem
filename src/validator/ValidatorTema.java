package validator;

import domain.Tema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Clasa Validator Tema
 * Validarea datelor de intrare pentru Tema
 */
public class ValidatorTema implements Validator<Tema> {
    private static Pattern idPtrn = Pattern.compile("^[1-9][0-9]*$");
    private static Pattern datePtrn = Pattern.compile("^[1-9]|1[0-4]$");
    /**
     * Validarea id-ului temei
     * @param id - numar intreg >0
     * @throws ValidationException daca id-ul nu e valid
     */
    private void validateId(String id){
        Matcher mtch = idPtrn.matcher(id);
        if(!mtch.matches()){
            throw new ValidationException("Id incorect!");
        }
    }
    /**
     * Validarea data
     * @param data - numar intreg >0
     * @throws ValidationException daca data nu e valida
     */
    private void validateDate(String data){
        Matcher mtch = datePtrn.matcher(data);
        if(!mtch.matches()){
            throw new ValidationException("Data incorecta!");
        }
    }

    /**
     * Se verifica daca data de predare < deadline
     * @param predare (data de primire a temei)
     * @param deadline (data limita de predare a temei)
     */
    private void validateInterval(String predare,String deadline){
        if(Integer.parseInt(predare)>Integer.parseInt(deadline))
            throw  new ValidationException("Deadline-ul nu poate fi mai mic ca data predarii!");
    }

    /**
     * Validarea entitatii student
     * @param entity - entitatea de validat
     * @throws ValidationException daca tema nu e valida
     */
    @Override
    public void validate(Tema entity) throws ValidationException {
        validateId(entity.getID());
        validateDate(entity.getDeadline());
        validateDate(entity.getDataPredare());
        validateInterval(entity.getDataPredare(),entity.getDeadline());
    }
}
