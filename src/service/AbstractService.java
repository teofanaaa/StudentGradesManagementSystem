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

public class AbstractService<ID , E extends HasID<ID>>  implements Service<ID,E>, Observable<DataChanged> {
    private Repository<ID,E> repository;
    protected ArrayList<Observer<DataChanged>> observers = new ArrayList<>();

    public AbstractService(Repository<ID, E> repository) {
        this.repository = repository;
    }

    @Override
    public Integer size() {
        return StreamSupport.stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toList())
                .size();
    }

    @Override
    public E add(E entity) throws ValidationException {
        Optional<E> e=repository.save(Optional.ofNullable(entity));
        if(!e.isPresent()){
            notifyObservers(new DataChanged(EventType.ADD));
            return null;
        }
        return e.get();
    }

    @Override
    public E remove(ID id){
        Optional<E> removed = repository.delete(Optional.of(id));
        if (removed.isPresent()){
            notifyObservers(new DataChanged(EventType.DELETE));
            return removed.get();
        }
        return null;
    }

    @Override
    public void removeAll() {
        for(E entity: repository.findAll()){
            repository.delete(Optional.of(entity.getID()));
            notifyObservers(new DataChanged(EventType.DELETE));
        }
    }

    @Override
    public E update(E entity) throws ValidationException{
        Optional<E> updated = repository.update(Optional.of(entity));
        if (!updated.isPresent()) {
            notifyObservers(new DataChanged(EventType.UPDATE));
            return null;
        }
        return updated.get();
    }

    @Override
    public E find(ID id){
        Optional<E> entity= repository.findOne(Optional.of(id));
        return entity.orElse(null);
    }

    @Override
    public ArrayList<E> getAll() {
        return (ArrayList<E>) StreamSupport.stream(repository.findAll().spliterator(),false)
                .collect(Collectors.toList());
    }

    //Observer

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
