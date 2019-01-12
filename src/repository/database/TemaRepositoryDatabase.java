package repository.database;

import domain.Tema;
import validator.ValidatorTema;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Clasa TemaRepositoryDatabase
 */
public class TemaRepositoryDatabase  extends AbstractRepositoryDatabase<String, Tema> {

    /**
     * Constructorul clasei
     * @param databaseCreator - database handler
     */
    public TemaRepositoryDatabase( DatabaseCreator databaseCreator) {
        super( new ValidatorTema(), databaseCreator, "Teme", "WHERE id=?");
    }

    /**
     * Creaza o tema din resultset
     * @param resultSet
     * @return Tema
     */
    @Override
    protected Optional<Tema> createEntityFromResultSet(ResultSet resultSet) {
        Optional<Tema> returned = Optional.empty();
        try {
            String id = resultSet.getString("id");
            String descriere = resultSet.getString("descriere");
            String deadline = resultSet.getString("deadline");
            String dataPredare = resultSet.getString("dataPredare");

            Tema tema = new Tema(id, descriere,deadline,dataPredare);

            return Optional.of(tema);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    /**
     * Cod sql de inserare a unei teme in baza de date
     * @throws SQLException
     */
    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO Teme (id, descriere, deadline,dataPredare) values (?, ?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    /**
     * Cod sql pentru actualizarea unei teme in baza de date
     * @param id - string (id-ul temei)
     * @throws SQLException
     */
    @Override
    protected void updateStatement(String id) throws SQLException {
        String query = "UPDATE Teme SET" +
                " id = ?, descriere = ?, deadline = ?, dataPredare = ?" +
                " WHERE id = ?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(5, id);
    }

    /**
     * Adaugarea valorilor atributelor in statementul curent
     * @param entity - tema de adaugat/actualizat
     * @throws SQLException
     */
    @Override
    protected void populateStatementValues(Tema entity) throws SQLException {
        preparedStatement.setString(1, entity.getID());
        preparedStatement.setString(2, entity.getDescriere());
        preparedStatement.setString(3, entity.getDeadline());
        preparedStatement.setString(4, entity.getDataPredare());
    }

    /**
     * Adaugarea id-urilor in statementul curent
     * @param s - string (id teme)
     * @throws SQLException
     */
    @Override
    protected void setID(String s) throws SQLException {
        preparedStatement.setString(1,s);
    }
}
