package repository.database;

import domain.Profesor;
import validator.ValidatorProfesor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProfesorRepositoryDatabase extends AbstractRepositoryDatabase<String, Profesor> {
    public ProfesorRepositoryDatabase(DatabaseCreator databaseCreator) {
        super(new ValidatorProfesor(), databaseCreator, "Profesori", "WHERE id = ?");
    }

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

    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO Profesori (id, nume, email) values (?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    @Override
    protected void updateStatement(String id) throws SQLException {
        String query = "UPDATE Profesori SET" +
                " id = ?, nume = ?, email = ?" +
                " WHERE id = ?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(4, id);
    }

    @Override
    protected void populateStatementValues(Profesor entity) throws SQLException {
        preparedStatement.setString(1, entity.getID());
        preparedStatement.setString(2, entity.getNume());
        preparedStatement.setString(3, entity.getEmail());
    }

    @Override
    protected void setID(String id) throws SQLException {
        preparedStatement.setString(1, ((String) id));
    }
}
