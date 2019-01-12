package repository.database;

import domain.Student;
import validator.Validator;
import validator.ValidatorStudent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Clasa StudentRepositoryDatabase
 */
public class StudentRepositoryDatabase extends AbstractRepositoryDatabase<String, Student> {
    /**
     * Constructorul clasei
     * @param databaseCreator
     */
    public StudentRepositoryDatabase( DatabaseCreator databaseCreator) {
        super( new ValidatorStudent(), databaseCreator, "Studenti", "WHERE id = ?");
    }

    /**
     * Creaza un student din resultset
     * @param resultSet
     * @return student
     */
    @Override
    protected Optional<Student> createEntityFromResultSet(ResultSet resultSet) {
        Optional<Student> returned = Optional.empty();

        try {
            String id = resultSet.getString("id");
            String nume = resultSet.getString("nume");
            String prenume = resultSet.getString("prenume");
            String grupa = resultSet.getString("grupa");
            String email = resultSet.getString("email");
            String prof = resultSet.getString("indrumatorLab");

            Student student = new Student(id, nume, prenume, grupa, email, prof);

            return Optional.of(student);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    /**
     * Cod sql de inserare a unui student in baza de date
     * @throws SQLException
     */
    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO Studenti (id, nume, prenume, grupa, email, indrumatorLab) values (?, ?, ?, ?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    /**
     * Cod sql pentru actualizarea unui in baza de date
     * @param id - string (id-ul studentului)
     * @throws SQLException
     */
    @Override
    protected void updateStatement(String id) throws SQLException {
        String query = "UPDATE Studenti SET" +
                " id = ?, nume = ?, prenume = ?, grupa = ?, email = ?, indrumatorLab = ?" +
                " WHERE id = ?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(7, id);
    }

    /**
     * Adaugarea valorilor atributelor in statementul curent
     * @param entity - studentul de adaugat/actualizat
     * @throws SQLException
     */
    @Override
    protected void populateStatementValues(Student entity) throws SQLException {
        preparedStatement.setString(1, entity.getID());
        preparedStatement.setString(2, entity.getNume());
        preparedStatement.setString(3, entity.getPrenume());
        preparedStatement.setString(4, entity.getGrupa());
        preparedStatement.setString(5, entity.getEmail());
        preparedStatement.setString(6, entity.getIndrumatorLab());
    }

    /**
     * Adaugarea id-urilor in statementul curent
     * @param id - string (id student)
     * @throws SQLException
     */
    @Override
    protected void setID(String id) throws SQLException{
        preparedStatement.setString(1, ((String) id));
    }
}
