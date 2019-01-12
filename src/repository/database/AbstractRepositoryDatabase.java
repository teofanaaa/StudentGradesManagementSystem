package repository.database;

import domain.HasID;
import javafx.util.Pair;
import repository.Repository;
import validator.ValidationException;
import validator.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Clasa AbstractRepositoryDatabase
 * @param <Id> id-ul entitatii
 * @param <E> entitatea de salvat in repository
 */
public abstract class AbstractRepositoryDatabase<Id, E extends HasID<Id>> implements Repository<Id,E> {
    protected String condition;
    protected Validator<E> validator;
    protected DatabaseCreator databaseCreator;
    protected String tableName;
    protected static PreparedStatement preparedStatement = null;
    protected String getAllQuery;

    /**
     * Constructorul clasei
     * @param validator - clasa validator
     * @param databaseCreator - batabase handler-ul
     * @param tableName - numele tabelului corespunzator entitatii de salvat
     * @param condition - conditia dupa care se face salvarea in baza de date (id-urile, unul sau mai multe)
     */
    public AbstractRepositoryDatabase(Validator<E> validator,
                                      DatabaseCreator databaseCreator, String tableName,String condition) {
        this.condition=condition;
        this.validator = validator;
        this.databaseCreator = databaseCreator;
        this.tableName = tableName;
        this.getAllQuery = "SELECT * FROM " + tableName;
    }

    /**
     * Creaza entitatea din result set
     * @param resultSet
     * @return entitate
     */
    protected abstract Optional<E> createEntityFromResultSet(ResultSet resultSet);

    /**
     * Insereaza o entitate in baza de date
     * @throws SQLException daca nu s-a putut insera
     */
    protected abstract void insertStatement() throws SQLException;

    /**
     * Updateaza datele unui entitati
     * @param id - id-ul entitatii
     * @throws SQLException
     */
    protected abstract void updateStatement(Id id) throws SQLException;

    /**
     * Adauga un rand in tabelul corespunzator
     * @param entity - entitatea de inregistrat
     * @throws SQLException
     */
    protected abstract void populateStatementValues(E entity) throws SQLException;

    /**
     * Seteaza id-ul entitatii
     * @param id
     * @throws SQLException
     */
    protected abstract void setID(Id id)throws SQLException;

    /**
     * Functia de cautare
     * @param id - string (id-ul entitatii de cautat)
     * @return entitatea
     */
    @Override
    public Optional<E> findOne(Optional<Id> id) {
        id.orElseThrow(()-> new IllegalArgumentException("Nu ai dat parametru"));
        try {
            String query = "SELECT *" +
                    " FROM " + tableName +" "+condition
                    ;

            preparedStatement =  DatabaseCreator.getConnection().prepareStatement(query);
            setID(id.get());

            ResultSet result = preparedStatement.executeQuery();

            Optional<E> returned = Optional.empty();
            if (result.next())
                returned = createEntityFromResultSet(result);
            return returned;
        } catch(SQLException ex) {
            System.err.println("Unable to execute query.\n" + ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Lista de entitati
     * @return iterable
     */
    @Override
    public Iterable<E> findAll() {
        ResultSet resultSet = databaseCreator.execQuery(getAllQuery);
        ArrayList<E> elements = new ArrayList<>();

        if (resultSet == null)
            return null;

        try {
            while (resultSet.next()) {
                Optional<E> entity = createEntityFromResultSet(resultSet);
                entity.ifPresent(elements::add);
            }
        } catch (SQLException e) {
            System.out.println("Eroare la findAll: " + e.getMessage());
            return new ArrayList<>();
        }

        if (elements.isEmpty())
            return null;

        return elements;
    }

    /**
     * Salvarea unei entitati in baza de date
     * @param entity - entitatea de salvat
     * entity must be not null
     * @return entitatea (daca nu s-a putut salva), null (entitatea a fost salvata)
     * @throws ValidationException (datele sunt invalide)
     */
    @Override
    public Optional<E> save(Optional<E> entity) throws ValidationException {
        entity.orElseThrow(()-> new IllegalArgumentException("Nu ai dat parametru"));
        try {
            validator.validate(entity.get());
            insertStatement();
            populateStatementValues(entity.get());
            preparedStatement.execute();
            return Optional.empty(); //s-a putut face inserul
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("duplicate key value"))
                System.err.println("Unable to execute query.\n" + ex.getMessage());
        }
        return entity;
    }

    /**
     * Sterge o intregistrare din baza de date
     * @param id - string (id-ul entitatii de sters)
     * @return entitatea stearasa / null (nu exista id-ul)
     */
    @Override
    public Optional<E> delete(Optional<Id> id) {
        id.orElseThrow(()-> new IllegalArgumentException("Nu ai dat parametru"));
        try {
            String queryGet = "SELECT * FROM " + tableName + " "+condition;
            String query = "DELETE FROM " + tableName + " "+condition;

            preparedStatement = DatabaseCreator.getConnection().prepareStatement(queryGet);

            setID(id.get());

            ResultSet result = preparedStatement.executeQuery();

            E returned = null;
            if (result.next()) {
                Optional<E> optionalReturned = createEntityFromResultSet(result);
                if (optionalReturned.isPresent())
                    returned = optionalReturned.get();
            }

            preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
            setID(id.get());
            preparedStatement.execute();

            return Optional.ofNullable(returned);

        } catch(SQLException ex) {
            System.err.println("Unable to execute query.\n" + ex.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Actualizare date entitate
     * @param entity - entitatea de actualizat
     * @return entitatea (entitatea nu exista)/null (entitatea a fost actualizata)
     */
    @Override
    public Optional<E> update(Optional<E> entity) {
        entity.orElseThrow(()-> new IllegalArgumentException("Nu ai dat parametru"));
        try {
            validator.validate(entity.get());

            String queryGet = "SELECT * FROM " + tableName + " "+condition;

            preparedStatement = DatabaseCreator.getConnection().prepareStatement(queryGet);
            setID(entity.get().getID());

            ResultSet result = preparedStatement.executeQuery();

            E returned = null;
            if (result.next()) {
                Optional<E> optionalReturned = createEntityFromResultSet(result);
                if (optionalReturned.isPresent())
                    returned = optionalReturned.get();
                updateStatement(entity.get().getID());
                populateStatementValues(entity.get());
                preparedStatement.execute();
                return Optional.empty();
            }
            return entity;


        } catch(SQLException ex) {
            System.err.println("Unable to execute query.\n" + ex.getMessage());
            return entity;
        }
    }
}