package service;

import domain.Profesor;
import domain.Student;
import repository.Repository;
import utils.PasswordStorage;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProfesorService extends AbstractService<String, Profesor> {
    Repository<String, Student> repoS;
    Repository<String, Profesor> repoP;
    UtilizatorService servUser;

    public ProfesorService(Repository<String, Profesor> repoP,Repository<String,Student> repoS,
                           UtilizatorService servUser) {
        super(repoP);
        this.repoP=repoP;
        this.repoS=repoS;
        this.servUser=servUser;
    }

    @Override
    public Profesor add(Profesor entity) throws ValidationException {
        try {
            servUser.createUser(entity);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
        return super.add(entity);
    }

    @Override
    public Profesor remove(String s) {
        String username=find(s).getEmail().split("@")[0];
        servUser.remove(username);
        return super.remove(s);
    }

    @Override
    public void removeAll() {
        for(Profesor profesor:super.getAll()){
            String username=profesor.getEmail().split("@")[0];
            servUser.remove(username);
        }
        super.removeAll();
    }

    @Override
    public Profesor update(Profesor entity) throws ValidationException {
        Profesor profesor=find(entity.getID());
        if(profesor!=null){
            if(entity.getNume().equals("")) entity.setNume(profesor.getNume());
            if(entity.getEmail().equals("")) entity.setEmail(profesor.getEmail());

            //Modificare user
            String username=profesor.getEmail().split("@")[0];
            servUser.remove(username);
            try {
                servUser.createUser(entity);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            }
        }

        return super.update(entity);
    }

    public List<String> getGrupeProf(String id){
        List<String> list = new ArrayList<>();

        repoS.findAll().forEach(student -> {
            if (! list.contains(student.getGrupa()) && student.getIndrumatorLab().contains(id))
                list.add(student.getGrupa());
        });
        list.sort(Comparator.naturalOrder());
        return list;
    }

}
