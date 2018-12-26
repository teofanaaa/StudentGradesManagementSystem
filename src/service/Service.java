package service;

import domain.HasID;
import validator.ValidationException;

import java.util.ArrayList;

public interface Service<ID, E extends HasID<ID>> {
    Integer size();

    E add(E entity) throws ValidationException;

    E remove(ID id);

    void removeAll();

    E update(E entity) throws ValidationException;

    E find(ID id);

    ArrayList<E> getAll();
}
