package service;

import domain.Nota;
import domain.Profesor;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import repository.Repository;
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
import java.util.stream.StreamSupport;

import static utils.Config.filterAndSorter;

/**
 * Clasa StudentService
 */
public class StudentService extends AbstractService<String, Student> {
    Repository<String, Tema> repoT;
    Repository<Pair<String,String>, Nota> repoN;
    UtilizatorService servUser;

    /**
     * Constructorul clasei
     * @param repoS - repository de student
     * @param repoT - repository de teme
     * @param repoN - repository de note
     * @param servUser - service de useri
     */
    public StudentService(Repository<String, Student> repoS, Repository<String, Tema> repoT,
                          Repository<Pair<String, String>, Nota> repoN, UtilizatorService servUser) {
        super(repoS);
        this.repoT = repoT;
        this.repoN = repoN;
        this.servUser=servUser;
    }

    /**
     * Testeaza daca exista username-ul
     * @param username - string
     * @return true/false
     */
    private boolean existaUsername(String username){
        return servUser.find(username) != null;
    }

    /**
     * Adauga entitatea in repo
     * @param entity (entitatea de adaugat)
     * @return entity (nu s-a putut adauga)/null (entitatea a fost adaugata)
     * @throws ValidationException (date invalide)
     */
    @Override
    public Student add(Student entity) throws ValidationException {
        Student returned=entity;
        if(entity.getEmail().contains("@"))
            if(existaUsername(entity.getEmail().split("@")[0])){
                GUIUtils.showErrorMessage("Man... dar ce facem aici? Furam date?");
                return returned;
            }

        try {
            returned=super.add(entity);
            if(returned!=null)
                GUIUtils.showErrorMessage("Exista deja un student cu id-ul dat!");
            else{
                servUser.createUser(entity);
            }
            notifyObservers(new DataChanged(EventType.ADD));
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }catch(ValidationException e){
            GUIUtils.showErrorMessage(e.getMessage());
        }
        return returned;
    }

    /**
     * Sterge un student
     * @param s -string (id student)
     * @return student (exista id-ul)/null (nu exista id-ul)
     */
    @Override
    public Student remove(String s) {
        String username=find(s).getEmail().split("@")[0];
        notifyObservers(new DataChanged(EventType.DELETE));
        stergeNote(s);
        Student returned= super.remove(s);
        if(returned!=null){
            servUser.remove(username);
            notifyObservers(new DataChanged(EventType.DELETE));
        }
        return returned;
    }

    /**
     * Sterge toti studentii
     */
    @Override
    public void removeAll() {
        for(Student student:super.getAll()){
            String username=student.getEmail().split("@")[0];
            servUser.remove(username);
            stergeNote(student.getID());
        }
        super.removeAll();
        notifyObservers(new DataChanged(EventType.DELETE));
    }

    /**
     * Actualizaza datele unui student
     * @param entity (entitatea de actualizat)
     * @return Student daca nu s-a putut actualiza / null (s-au actualizat datele)
     * @throws ValidationException (date invalide)
     */
    @Override
    public Student update(Student entity) throws ValidationException {
        try{
            Student student=find(entity.getID());
            if(student!=null){
                if(entity.getNume().equals("")) entity.setNume(student.getNume());
                if(entity.getPrenume().equals("")) entity.setPrenume(student.getPrenume());
                if(entity.getGrupa().equals("")) entity.setGrupa(student.getGrupa());
                if(entity.getEmail().equals("")) entity.setEmail(student.getEmail());
                if(entity.getIndrumatorLab().equals("")) entity.setIndrumatorLab(student.getIndrumatorLab());

                //Modificare user
                String username = student.getEmail().split("@")[0];
                servUser.remove(username);
                servUser.createUser(entity);
            }
            Student returned=super.update(entity);
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
     * Filtare studenti dupa id profesor
     * @param profesor - string (id profesor)
     * @return lista de studenti
     */
    public List<Student> filtreazaStudentiProf(String profesor) {
        return filterAndSorter(getAll(), entity -> entity.getIndrumatorLab().contains(profesor), null);
    }

    /**
     * Filtrare studenti dupa grupa
     * @param grupa - string
     * @return - lista de studenti
     */
    public List<Student> filtreazaStudentiGrupa(String grupa) {
        return filterAndSorter(getAll(), entity -> entity.getGrupa().equals(grupa), Comparator.comparing(Student::getNume));
    }

    /**
     * Filtare studentii unui profesor dupa keyword
     * @param idProfesor - string
     * @param keyword -string
     * @return lista de studenti
     */
    public List<Student> filtreazaStudentiProfKeyword(String idProfesor,String keyword){
        return filtreazaStudentiKeyword(keyword)
                .stream()
                .filter(s->s.getIndrumatorLab().equals(idProfesor))
                .collect(Collectors.toList());
    }

    /**
     * Filtreaza toti studentii dupa keyword
     * @param keyword - string
     * @return lista de studenti
     */
    public List<Student> filtreazaStudentiKeyword(String keyword) {
        return filterAndSorter(getAll(),
                entity -> entity.getID().contains(keyword.toLowerCase()) ||
                        entity.getGrupa().contains(keyword.toLowerCase()) ||
                        entity.getPrenume().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getIndrumatorLab().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                        entity.getNume().toLowerCase().contains(keyword.toLowerCase()),
                Comparator.comparing(Student::getNume));
    }

    /**
     * Lista de grupe
     * @return lista de stringuri
     */
    public List<String> getGrupe(){
        List<String> list = new ArrayList<>();
        getAll().forEach(student -> {
            if (! list.contains(student.getGrupa()))
                list.add(student.getGrupa());
        });
        list.sort(Comparator.naturalOrder());
        return list;
    }

    /**
     * Lista de email-uri studenti
     * @return lista de stringuri
     */
    public List<String> listaEmailuri(){
        return getAll().stream()
                .map(Student::getEmail)
                .collect(Collectors.toList());
    }

    /**
     * Cautare student dupa username
     * @param username - string
     * @return student
     */
    public Student findByUsername(String username){
        return getAll()
                .stream()
                .filter(s->s.getEmail().split("@")[0].equals(username))
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Sterge notele unui student
     * @param idStundet - string (id student)
     */
    private void stergeNote(String idStundet){
        for(Tema tema:repoT.findAll()){
            Pair<String, String> id=new Pair(idStundet,tema.getID());
            if(repoN.findOne(Optional.ofNullable(id)).isPresent())
                repoN.delete(Optional.of(id));
        }
    }


}
