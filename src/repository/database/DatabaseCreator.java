package repository.database;

import java.sql.*;

/**
 * Clasa DatabaseCreator
 */
public final class DatabaseCreator {
    private static String dbURL ;
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;

    /**
     * Constructorul clasei
     * @param locatie - string (locatia unde urmeaza sa fie creata baza de date)
     */
    public DatabaseCreator(String locatie) {
        createConnection(locatie);
        createTable("Profesori", profesoriTable());
        createTable("Studenti",studentiTable());
        createTable("Teme",temeTable());
        createTable("Note",noteTable());
        createTable("Utilizatori",utilizatoriTable());

        //shutdown();
    }

    /**
     * Getter conexiune
     * @return
     */
    static Connection getConnection() {
        return conn;
    }

    /**
     * Crearea conexiunii la locatia data
     * @param locatie - string (locatia unde urmeaza sa fie creata baza de date)
     */
    private static void createConnection(String locatie)
    {
        try
        {
            dbURL= "jdbc:derby:"+locatie+";create=true";
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").getConstructor().newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL);
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }

    /**
     * Crearea tabelului corespunzator entitatii Profesor
     * @return string (cod sql)
     */
    private String profesoriTable(){
        return "CREATE TABLE PROFESORI (" +
                " id VARCHAR(10) primary key," +
                " nume VARCHAR(100)," +
                " email VARCHAR(200)" +
                ")";
    }

    /**
     * Crearea tabelului corespunzator entitatii Student
     * @return string (cod sql)
     */
    private String studentiTable(){
        return "CREATE TABLE STUDENTI (" +
                " id VARCHAR(7) primary key," +
                " nume VARCHAR(100)," +
                " prenume VARCHAR(100)," +
                " grupa VARCHAR(3)," +
                " email VARCHAR(200)," +
                " indrumatorLab VARCHAR(10)," +
                " CONSTRAINT prof_FK FOREIGN KEY (indrumatorLab)" +
                " REFERENCES PROFESORI (id)" +
                ")";
    }

    /**
     * Crearea tabelului corespunzator entitatii Tema
     * @return string (cod sql)
     */
    private String temeTable(){
        return "CREATE TABLE TEME (" +
                " id VARCHAR(4) primary key," +
                " descriere VARCHAR(200)," +
                " deadline VARCHAR(2), " +
                " dataPredare VARCHAR(2) " +
                ")";
    }

    private String noteTable(){
        return "CREATE TABLE NOTE (" +
                " studentID VARCHAR(7) not null,\n" +
                " temaID VARCHAR(4) not null,\n" +
                " dataCurenta VARCHAR(2),\n" +
                " notaProf VARCHAR(8),\n" +

                " CONSTRAINT NOTE_PK PRIMARY KEY (studentID, temaID), \n" +

                " CONSTRAINT STD_FK\n" +
                " FOREIGN KEY (studentID)\n" +
                " REFERENCES STUDENTI (id)," +

                " CONSTRAINT TEME_FK\n" +
                " FOREIGN KEY (temaID)\n" +
                " REFERENCES  TEME(id)"+
                ")";
    }

    /**
     * Crearea tabelului corespunzator entitatii Utilizator
     * @return string (cod sql)
     */
    private String utilizatoriTable(){
        return "CREATE TABLE UTILIZATORI (" +
                " username VARCHAR(100) primary key," +
                " hash VARCHAR(200), " +
                " tip VARCHAR(50), " +
                " nume VARCHAR(200) " +
                ")";
    }

    /**
     * Creare tabel
     * @param table - string (numele tabelului)
     * @param code - string (cod sql corespunzator crearii tabelului)
     */
    private void createTable(String table,String code) {
        try {
            stmt = conn.createStatement();

            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet tables = metadata.getTables(null, null, table.toUpperCase(), null);

            if (! tables.next()) {
                stmt.execute(code);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        }
    }

    /**
     * Executie query
     * @param query - string (codul sql al query-ului)
     * @return resultset
     */
    ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * Executa actiunea corespunzatoarea query-ului
     * @param query - string (cod sql)
     * @return boolean (T: s-a executat query-ul, F: nu s-a putut executa query-ul)
     */
    public boolean execAction(String query) {
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Shotdown la baza de date
     */
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }
        }
        catch (SQLException ignored)
        {
        }

    }
}
