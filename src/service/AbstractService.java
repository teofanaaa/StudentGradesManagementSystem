package service;

import domain.HasID;
import repository.Repository;
import utils.DataChanged;
import utils.EventType;
import utils.Observable;
import utils.Observer;
import validator.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Clasa AbstractService
 * @param <ID> (id-ul entitatii)
 * @param <E> (entitatea)
 */
public class AbstractService<ID , E extends HasID<ID>>  implements Service<ID,E>, Observable<DataChanged> {
    private Repository<ID,E> repository;
    protected ArrayList<Observer<DataChanged>> observers = new ArrayList<>();

    /**
     * Constructorul clasei
     * @param repository
     */
    public AbstractService(Repository<ID, E> repository) {
        this.repository = repository;
    }

    /**
     * Dimensiunea listei de entitati
     * @return integer
     */
    @Override
    public Integer size() {
        return StreamSupport.stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Adauga entitatea in service
     * @param entity (entitatea de adaugat)
     * @return entitatea de adaugat (nu sa putut face adaugarea)/ null(entitatea a fost adaugata)
     * @throws ValidationException (date invalide)
     */
    @Override
    public E add(E entity) throws ValidationException {
        Optional<E> e=repository.save(Optional.ofNullable(entity));
        if(!e.isPresent()){
            notifyObservers(new DataChanged(EventType.ADD));
            return null;
        }
        return e.get();
    }

    /**
     * Sterge o entitate din service
     * @param id - ID (id-ul entitatii)
     * @return entitate (daca id-ul exista)/ null (nu esita id-ul)
     */
    @Override
    public E remove(ID id){
        Optional<E> removed = repository.delete(Optional.of(id));
        if (removed.isPresent()){
            notifyObservers(new DataChanged(EventType.DELETE));
            return removed.get();
        }
        return null;
    }

    /**
     * Sterge toate entitatile din lista
     */
    @Override
    public void removeAll() {
        for(E entity: repository.findAll()){
            repository.delete(Optional.of(entity.getID()));
            notifyObservers(new DataChanged(EventType.DELETE));
        }
    }

    /**
     * Actulaizeaza o entitate
     * @param entity (entitatea de actualizat)
     * @return entitatea (nu exista id-ul)/null (entitatea a fost adaugata)
     * @throws ValidationException (date invalide)
     */
    @Override
    public E update(E entity) throws ValidationException{
        Optional<E> updated = repository.update(Optional.of(entity));
        if (!updated.isPresent()) {
            notifyObservers(new DataChanged(EventType.UPDATE));
            return null;
        }
        return updated.get();
    }

    /**
     * Cauta entitatea cu id-ul dat
     * @param id - ID (id-ul entitatii)
     * @return entitatea / null (nu exista id-ul cautat)
     */
    @Override
    public E find(ID id){
        Optional<E> entity= repository.findOne(Optional.of(id));
        return entity.orElse(null);
    }

    /**
     * Getter pentru toate entitatile
     * @return lista de entitati
     */
    @Override
    public ArrayList<E> getAll() {
        return (ArrayList<E>) StreamSupport.stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    //Observer

    //Implementarea metodelor

    @Override
    public void addObserver(Observer<DataChanged> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<DataChanged> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(DataChanged t) {
        observers.forEach(o->o.update(t));
    }
}
