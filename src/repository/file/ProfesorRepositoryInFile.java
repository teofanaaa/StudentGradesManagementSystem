package repository.file;

import domain.Profesor;
import validator.Validator;

public class ProfesorRepositoryInFile extends AbstractRepositoryInFile<String, Profesor> {
    /**
     * Constructorul clasei
     *
     * @param fileName  - String (numele fisierului)
     * @param validator - Validator (clasa Validator, validare date)
     */
    public ProfesorRepositoryInFile(String fileName, Validator<Profesor> validator) {
        super(fileName, validator);
    }

    @Override
    public Profesor extractEntity(String line) {
        String[] parts=line.split("/");
        return new Profesor(parts[0],parts[1],parts[2]);
    }
}
