package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import repository.Repository;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentService extends AbstractService<String, Student> {
    Repository<String, Student> repoS;
    Repository<String, Tema> repoT;
    Repository<Pair<String,String>, Nota> repoN;

    public StudentService(Repository<String, Student> repoS, Repository<String, Tema> repoT,
                          Repository<Pair<String, String>, Nota> repoN) {
        super(repoS);
        this.repoS=repoS;
        this.repoT = repoT;
        this.repoN = repoN;
    }

    @Override
    public Student update(Student entity) throws ValidationException {
        Student student=find(entity.getID());
        if(student!=null){
            if(entity.getNume().equals("")) entity.setNume(student.getNume());
            if(entity.getPrenume().equals("")) entity.setPrenume(student.getPrenume());
            if(entity.getGrupa().equals("")) entity.setGrupa(student.getGrupa());
            if(entity.getEmail().equals("")) entity.setEmail(student.getEmail());
            if(entity.getIndrumatorLab().equals("")) entity.setIndrumatorLab(student.getIndrumatorLab());
        }
        return super.update(entity);
    }

    public List<String> getGrupe(){
        List<String> list = new ArrayList<>();

        getAll().forEach(student -> {
            if (! list.contains(student.getGrupa()))
                list.add(student.getGrupa());
        });
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public List<String> getGrupeProf(String prof){
        List<String> list = new ArrayList<>();

        getAll().forEach(student -> {
            if (! list.contains(student.getGrupa()) && student.getIndrumatorLab().contains(prof))
                list.add(student.getGrupa());
        });
        list.sort(Comparator.naturalOrder());
        return list;
    }
}
