package repository.database;

import domain.Nota;
import javafx.util.Pair;
import validator.ValidatorNota;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Clasa NotaRepositoryDatabase
 */
public class NotaRepositoryDatabase extends AbstractRepositoryDatabase<Pair<String,String>, Nota>{

    /**
     * Constructorul clasei
     * @param databaseCreator
     */
    public NotaRepositoryDatabase(DatabaseCreator databaseCreator) {
        super(new ValidatorNota(), databaseCreator, "Note","WHERE studentID = ? and temaID = ?");
    }

    /**
     * Creaza o nota din resultset
     * @param resultSet
     * @return nota
     */
    @Override
    protected Optional<Nota> createEntityFromResultSet(ResultSet resultSet) {
        Optional<Nota> returned = Optional.empty();

        try {
            String id_student = resultSet.getString("studentID");
            String id_tema = resultSet.getString("temaID");
            String saptamana = resultSet.getString("dataCurenta");
            String nota = resultSet.getString("notaProf");

            Nota notaObject = new Nota( id_student, id_tema, saptamana,nota);

            return Optional.of(notaObject);
        } catch (SQLException e) {
            System.out.println("Eroare la create entity din result set : " + e.getMessage());
        }

        return returned;
    }

    /**
     * Cod sql de inserare a unei note in baza de date
     * @throws SQLException
     */
    @Override
    protected void insertStatement() throws SQLException {
        String query = "INSERT INTO " + tableName +
                " (studentID, temaID, dataCurenta, notaProf) values (?, ?, ?, ?)";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
    }

    /**
     * Cod sql pentru actualizarea unei note in baza de date
     * @param stringStringPair - pair (id-ul notei)
     * @throws SQLException
     */
    @Override
    protected void updateStatement(Pair<String, String> stringStringPair) throws SQLException {
        String query = "UPDATE " + tableName + " SET" +
                " studentID=?, temaID=?, dataCurenta=?, notaProf=?" +
                " WHERE studentID = ? and temaID=?";
        preparedStatement = DatabaseCreator.getConnection().prepareStatement(query);
        preparedStatement.setString(5, stringStringPair.getKey());
        preparedStatement.setString(6, stringStringPair.getValue());
    }

    /**
     * Adaugarea valorilor atributelor in statementul curent
     * @param entity - nota de adaugat/actualizat
     * @throws SQLException
     */
    @Override
    protected void populateStatementValues(Nota entity) throws SQLException {
        preparedStatement.setString(1, entity.getStudentID());
        preparedStatement.setString(2, entity.getTemaID());
        preparedStatement.setString(3, entity.getDataCurenta());
        preparedStatement.setString(4, entity.getNotaProf());
    }

    /**
     * Adaugarea id-urilor in statementul curent
     * @param stringStringPair
     * @throws SQLException
     */
    @Override
    protected void setID(Pair<String, String> stringStringPair) throws SQLException {
        preparedStatement.setString(1,stringStringPair.getKey());
        preparedStatement.setString(2,stringStringPair.getValue());
    }

}
