package repository;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import validator.Validator;

public class NotaRepositoryInFile extends AbstractRepositoryInFile<Pair<Student,Tema>, Nota>  {
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
        Student s=new Student(parts[0],"fake","fake","222","fake@yahoo.com","fake");
        Tema t=new Tema(parts[1],"fara descriere","1","1");
        return new Nota(s,t,parts[2],parts[3]);

    }
}
