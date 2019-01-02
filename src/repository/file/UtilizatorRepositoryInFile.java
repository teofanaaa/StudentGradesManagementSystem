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
        String hash="";
        for(int i=1;i<size-2;i++){
            hash=hash+parts[i]+"/";
        }
        String hashBun=hash.substring(0,hash.length()-1);
        return new Utilizator(parts[0],hashBun, Utilizator.TipUtilizator.valueOf(parts[size-2]),parts[size-1]);
    }
}
