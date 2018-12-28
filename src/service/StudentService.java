package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import repository.Repository;
import utils.PasswordStorage;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Config.filterAndSorter;

public class StudentService extends AbstractService<String, Student> {
    Repository<String, Tema> repoT;
    Repository<Pair<String,String>, Nota> repoN;
    UtilizatorService servUser;

    public StudentService(Repository<String, Student> repoS, Repository<String, Tema> repoT,
                          Repository<Pair<String, String>, Nota> repoN, UtilizatorService servUser) {
        super(repoS);
        this.repoT = repoT;
        this.repoN = repoN;
        this.servUser=servUser;
    }

    @Override
    public Student add(Student entity) throws ValidationException {
        try {
            servUser.createUser(entity);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
        return super.add(entity);
    }

    @Override
    public Student remove(String s) {
        String username=find(s).getEmail().split("@")[0];
        servUser.remove(username);
        return super.remove(s);
    }

    @Override
    public void removeAll() {
        for(Student student:super.getAll()){
            String username=student.getEmail().split("@")[0];
            servUser.remove(username);
        }
        super.removeAll();
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

            //Modificare user
            String username=student.getEmail().split("@")[0];
            servUser.remove(username);
            try {
                servUser.createUser(entity);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            }
        }

        return super.update(entity);
    }

    public List<Student> filtreazaStudentiProf(String profesor) {
        return filterAndSorter(getAll(), entity -> entity.getIndrumatorLab().contains(profesor), null);
    }

    public List<Student> filtreazaStudentiGrupa(String grupa) {
        return filterAndSorter(getAll(), entity -> entity.getGrupa().equals(grupa), Comparator.comparing(Student::getNume));
    }

    public List<Student> filtreazaStudentiKeyword(String keyword) {
        return filterAndSorter(getAll(),
                entity -> String.valueOf(entity.getID()).contains(keyword) ||
                        String.valueOf(entity.getGrupa()).contains(keyword) ||
                        entity.getIndrumatorLab().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getNume().toLowerCase().contains(keyword.toLowerCase()),
                Comparator.comparing(Student::getNume));
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

    public List<String> listaEmailuri(){
        return getAll().stream()
                .map(Student::getEmail)
                .collect(Collectors.toList());
    }

}
