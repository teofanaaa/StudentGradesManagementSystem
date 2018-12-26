package repository.file;

import domain.Nota;
import javafx.util.Pair;
import repository.file.AbstractRepositoryInFile;
import validator.Validator;

public class NotaRepositoryInFile extends AbstractRepositoryInFile<Pair<String,String>, Nota> {
    /**
     * Constructorul clasei
     *
     * @param fileName  - String (numele fisierului)
     * @param validator - Validator (clasa Validator, validare date)
     */
    public NotaRepositoryInFile(String fileName, Validator<Nota> validator) {
        super(fileName, validator);
    }

    @Override
    public Nota extractEntity(String line) {
        String[] parts=line.split("/");
        return new Nota(parts[0],parts[1],parts[2],parts[3]);
    }
}
