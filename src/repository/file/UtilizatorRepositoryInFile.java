package repository.file;

import domain.Utilizator;
import validator.Validator;

public class UtilizatorRepositoryInFile extends AbstractRepositoryInFile<String, Utilizator>  {
    /**
     * Constructorul clasei
     *
     * @param fileName  - String (numele fisierului)
     * @param validator - Validator (clasa Validator, validare date)
     */
    public UtilizatorRepositoryInFile(String fileName, Validator<Utilizator> validator) {
        super(fileName, validator);
    }

    @Override
    public Utilizator extractEntity(String line) {
        String[] parts=line.split("/");
        int size=parts.length;
        return new Utilizator(parts[0],parts[1], Utilizator.TipUtilizator.valueOf(parts[size-2]),parts[size-1]);
    }
}
