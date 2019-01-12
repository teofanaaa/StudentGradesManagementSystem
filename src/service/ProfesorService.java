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

/**
 * Clasa ProfesorService
 */
public class ProfesorService extends AbstractService<String, Profesor> {
    Repository<String, Student> repoS;
    Repository<Pair<String,String>, Nota> repoN;
    UtilizatorService servUser;

    /**
     * Constructorul clasei
     * @param repoP - repository de profesori
     * @param repoS - repository de studenti
     * @param repoN - repository de note
     * @param servUser - service de useri
     */
    public ProfesorService(Repository<String, Profesor> repoP, Repository<String, Student> repoS,
                           Repository<Pair<String,String>, Nota> repoN,
                           UtilizatorService servUser) {
        super(repoP);
        this.repoS = repoS;
        this.repoN=repoN;
        this.servUser = servUser;
    }

    /**
     * Adaugare profesor
     * @param entity (entitatea de adaugat)
     * @return entuty(nu sa putut adauga profesorul)/ null (profesor adaugat)
     * @throws ValidationException (date invalide)
     */
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

    /**
     * Stergere profesor
     * @param s - string (id profesor)
     * @return null (nu s-a gasit id-ul)/ Profesor (profesorul sters)
     */
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

    /**
     * Sterge toata lista de profesori
     */
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

    /**
     * Actualizare date profesor
     * @param entity (entitatea de actualizat)
     * @return entity(nu s-a putut actualiza)/ null (date actualizate)
     * @throws ValidationException (date invalide)
     */
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

    /**
     * Lista de grupe a unui profesor
     * @param id - string (id profesor)
     * @return lista de grupe la care proda proful
     */
    public List<String> getGrupeProf(String id){
        List<String> list = new ArrayList<>();

        repoS.findAll().forEach(student -> {
            if (! list.contains(student.getGrupa()) && student.getIndrumatorLab().contains(id))
                list.add(student.getGrupa());
        });
        list.sort(Comparator.naturalOrder());
        return list;
    }

    /**
     * Cautare profesor dupa nume utilizator
     * @param username - string(nume utilizator)
     * @return Profesor
     */
    public Profesor findByUsername(String username){
        return getAll()
                .stream()
                .filter(s->s.getEmail().split("@")[0].equals(username))
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Creare unei liste de forma [nume]:[id] (pentru combobox)
     * @return lista de stringuri
     */
    public List<String> listaNumeId(){
        return getAll()
                .stream()
                .map(p->p.getNume()+":"+p.getID())
                .collect(Collectors.toList());
    }

    /**
     * Sterge studentii unui profesor
     * @param idProf - string (id profesor)
     */
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
