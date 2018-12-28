package repository.database;

import domain.Utilizator;
import validator.Validator;
import validator.ValidatorUtilizator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UtilizatorRepositoryDatabase extends AbstractRepositoryDatabase<String, Utilizator> {

    public UtilizatorRepositoryDatabase(DatabaseCreator databaseCreator) {
        super(new ValidatorUtilizator(), databaseCreator, "Utilizatori", "WHERE username = ?");
    }

    @Override
    protected Optional<Utilizator> createEntityFromResultSet(ResultSet resultSet) {
        Optional<Utilizator> returned = Optional.empty();

        try {
            String username = resultSet.getString("username");
            String hash = resultSet.getString("hash");
            String tip = resultSet.getString("tip");
            String nume = resultSet.getString("nume");

            Utilizator user = new Utilizator(username,hash, Utilizator.TipUtilizator.valueOf(tip),nume);

            return Optional.of(user);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO Utilizatori (username,hash,tip,nume) values (?, ?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    @Override
    protected void updateStatement(String username) throws SQLException {
        String query = "UPDATE Utilizatori SET" +
                " username=?, hash=?, tip=?, nume=?" +
                " WHERE username = ?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(5, username);
    }

    @Override
    protected void populateStatementValues(Utilizator entity) throws SQLException {
        preparedStatement.setString(1, entity.getID());
        preparedStatement.setString(2, entity.getHash());
        preparedStatement.setString(3, entity.getTip().toString());
        preparedStatement.setString(4, entity.getNume());
    }

    @Override
    protected void setID(String username) throws SQLException {
        preparedStatement.setString(1, ((String) username));
    }
}
