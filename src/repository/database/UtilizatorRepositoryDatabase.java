package repository.database;

import domain.Utilizator;
import validator.Validator;
import validator.ValidatorUtilizator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Clasa UtilizatorRepositoryDatabase
 */
public class UtilizatorRepositoryDatabase extends AbstractRepositoryDatabase<String, Utilizator> {

    /**
     * Constructorul clasei
     * @param databaseCreator - database handler
     */
    public UtilizatorRepositoryDatabase(DatabaseCreator databaseCreator) {
        super(new ValidatorUtilizator(), databaseCreator, "Utilizatori", "WHERE username = ?");
    }

    /**
     * Creaza un utilizator din resultset
     * @param resultSet
     * @return Utilizator
     */
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

    /**
     * Cod sql de inserare a unui utilizator in baza de date
     * @throws SQLException
     */
    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO Utilizatori (username,hash,tip,nume) values (?, ?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    /**
     * Cod sql pentru actualizarea unui utilizator in baza de date
     * @param username - string (id-ul utilizator)
     * @throws SQLException
     */
    @Override
    protected void updateStatement(String username) throws SQLException {
        String query = "UPDATE Utilizatori SET" +
                " username=?, hash=?, tip=?, nume=?" +
                " WHERE username = ?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(5, username);
    }

    /**
     * Adaugarea valorilor atributelor in statementul curent
     * @param entity - utilizatorul de adaugat/actualizat
     * @throws SQLException
     */
    @Override
    protected void populateStatementValues(Utilizator entity) throws SQLException {
        preparedStatement.setString(1, entity.getID());
        preparedStatement.setString(2, entity.getHash());
        preparedStatement.setString(3, entity.getTip().toString());
        preparedStatement.setString(4, entity.getNume());
    }

    /**
     * Adaugarea id-urilor in statementul curent
     * @param username - string (id utilizator)
     * @throws SQLException
     */
    @Override
    protected void setID(String username) throws SQLException {
        preparedStatement.setString(1, ((String) username));
    }
}
