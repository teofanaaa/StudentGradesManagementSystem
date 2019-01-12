package repository.database;

import domain.Profesor;
import validator.ValidatorProfesor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Clasa ProfesorRepositoryDatabase
 */
public class ProfesorRepositoryDatabase extends AbstractRepositoryDatabase<String, Profesor> {
    /**
     * Constructorul clasei
     * @param databaseCreator - database handler
     */
    public ProfesorRepositoryDatabase(DatabaseCreator databaseCreator) {
        super(new ValidatorProfesor(), databaseCreator, "Profesori", "WHERE id = ?");
    }

    /**
     * Creaza un profesor din resultset
     * @param resultSet
     * @return Profesor
     */
    @Override
    protected Optional<Profesor> createEntityFromResultSet(ResultSet resultSet) {
        Optional<Profesor> returned = Optional.empty();

        try {
            String id = resultSet.getString("id");
            String nume = resultSet.getString("nume");
            String email = resultSet.getString("email");

            Profesor student = new Profesor(id, nume, email);

            return Optional.of(student);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }
    /**
     * Cod sql de inserare a unui profesor in baza de date
     * @throws SQLException
     */
    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO Profesori (id, nume, email) values (?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    /**
     * Cod sql pentru actualizarea unui profesor in baza de date
     * @param id - string (id-ul profesorului)
     * @throws SQLException
     */
    @Override
    protected void updateStatement(String id) throws SQLException {
        String query = "UPDATE Profesori SET" +
                " id = ?, nume = ?, email = ?" +
                " WHERE id = ?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(4, id);
    }

    /**
     * Adaugarea valorilor atributelor in statementul curent
     * @param entity - profesorul de adaugat/actualizat
     * @throws SQLException
     */
    @Override
    protected void populateStatementValues(Profesor entity) throws SQLException {
        preparedStatement.setString(1, entity.getID());
        preparedStatement.setString(2, entity.getNume());
        preparedStatement.setString(3, entity.getEmail());
    }

    /**
     * Adaugarea id-urilor in statementul curent
     * @param id - string (id profesor)
     * @throws SQLException
     */
    @Override
    protected void setID(String id) throws SQLException {
        preparedStatement.setString(1, ((String) id));
    }
}
