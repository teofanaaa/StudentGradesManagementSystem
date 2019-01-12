package service;

import domain.HasID;
import validator.ValidationException;

import java.util.ArrayList;

/**
 * Interfata Service
 * @param <ID> id-ul entitatii
 * @param <E> entitatea
 */
public interface Service<ID, E extends HasID<ID>> {
    /**
     * Dimensiunea listei de entitati
     * @return integer
     */
    Integer size();

    /**
     * Adauga entitatea in service
     * @param entity (entitatea de adaugat)
     * @return entitatea de adaugat (nu sa putut face adaugarea)/ null(entitatea a fost adaugata)
     * @throws ValidationException (date invalide)
     */
    E add(E entity) throws ValidationException;

    /**
     * Sterge o entitate din service
     * @param id - ID (id-ul entitatii)
     * @return entitate (daca id-ul exista)/ null (nu esita id-ul)
     */
    E remove(ID id);

    /**
     * Sterge toate entitatile din lista
     */
    void removeAll();

    /**
     * Actulaizeaza o entitate
     * @param entity (entitatea de actualizat)
     * @return entitatea (nu exista id-ul)/null (entitatea a fost adaugata)
     * @throws ValidationException (date invalide)
     */
    E update(E entity) throws ValidationException;

    /**
     * Cauta entitatea cu id-ul dat
     * @param id - ID (id-ul entitatii)
     * @return entitatea / null (nu exista id-ul cautat)
     */
    E find(ID id);

    /**
     * Getter pentru toate entitatile
     * @return lista de entitati
     */
    ArrayList<E> getAll();
}
