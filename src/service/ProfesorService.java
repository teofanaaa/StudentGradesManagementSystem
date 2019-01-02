package service;

import domain.Nota;
import domain.Profesor;
import domain.Student;
import javafx.util.Pair;
import repository.Repository;
import repository.RepositoryException;
import utils.DataChanged;
import utils.EventType;
import utils.GUIUtils;
import utils.PasswordStorage;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfesorService extends AbstractService<String, Profesor> {
    Repository<String, Student> repoS;
    Repository<Pair<String,String>, Nota> repoN;
    UtilizatorService servUser;

    public ProfesorService(Repository<String, Profesor> repoP, Repository<String, Student> repoS,
                           Repository<Pair<String,String>, Nota> repoN,
                           UtilizatorService servUser) {
        super(repoP);
        this.repoS = repoS;
        this.repoN=repoN;
        this.servUser = servUser;
    }

    @Override
    public Profesor add(Profesor entity) throws ValidationException {
        Profesor returned=entity;
        try {

            returned= super.add(entity);
            if(returned!=null)
                GUIUtils.showErrorMessage("Exista deja un profesor cu id-ul dat!");
            else{
                servUser.createUser(entity);
                notifyObservers(new DataChanged(EventType.ADD));
            }
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }catch(ValidationException e){
            GUIUtils.showErrorMessage(e.getMessage());
        }

        return returned;
    }

    @Override
    public Profesor remove(String s) {
        String username=find(s).getEmail().split("@")[0];
        servUser.remove(username);
        Profesor returned= super.remove(s);
        if(returned!=null){
            stergeStudenti(s);
            notifyObservers(new DataChanged(EventType.DELETE));
        }
        return returned;
    }

    @Override
    public void removeAll() {
        for(Profesor profesor:super.getAll()){
            String username=profesor.getEmail().split("@")[0];
            servUser.remove(username);
            stergeStudenti(profesor.getID());
        }
        super.removeAll();
        notifyObservers(new DataChanged(EventType.DELETE));
    }

    @Override
    public Profesor update(Profesor entity) throws ValidationException {
        try {
            Profesor profesor = find(entity.getID());
            if (profesor != null) {
                if (entity.getNume().equals("")) entity.setNume(profesor.getNume());
                if (entity.getEmail().equals("")) entity.setEmail(profesor.getEmail());

                //Modificare user
                String username = profesor.getEmail().split("@")[0];
                servUser.remove(username);
                servUser.createUser(entity);
            }
            Profesor returned=super.update(entity);
            if(returned==null)notifyObservers(new DataChanged(EventType.UPDATE));
            return returned;
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
        catch (ValidationException e){
            GUIUtils.showErrorMessage(e.getMessage());
        }
        return entity;
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

    public Profesor findByUsername(String username){
        return getAll()
                .stream()
                .filter(s->s.getEmail().split("@")[0].equals(username))
                .collect(Collectors.toList())
                .get(0);
    }

    public List<String> listaNumeId(){
        return getAll()
                .stream()
                .map(p->p.getNume()+":"+p.getID())
                .collect(Collectors.toList());
    }

    private void stergeStudenti(String idProf){
        for(Student student:repoS.findAll()){
            if(student.getIndrumatorLab().equals(idProf)) {
                repoS.delete(Optional.of(student.getID()));
                servUser.remove(student.getEmail().split("@")[0]);
                repoN.delete(Optional.of(new Pair(student.getID(),idProf)));
            }
        }
    }

}
