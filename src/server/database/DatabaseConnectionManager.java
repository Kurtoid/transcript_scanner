package server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * singleton to manage connection to server's database
 *
 * @author Kurt Wilson
 */
public class DatabaseConnectionManager {
    private static DatabaseConnectionManager dbObject = null;
    Logger logger = Logger.getLogger(this.getClass().getName());
    private Connection con;

    /**
     * connects to server defined in default_server_settings.conf
     *
     * @throws SQLException
     */
    private DatabaseConnectionManager() throws SQLException {

        final String DB_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=ApplicationServer";

        String username = "sa";
        String password = "Suncoast$1";
        logger.log(Level.INFO, "Connecting with username and password");
        con = DriverManager.getConnection(DB_URL, username, password);

        logger.log(Level.INFO, "Connected");
        try {
            if (con != null) {
                DatabaseMetaData dm = con.getMetaData();
                logger.log(Level.INFO, "Driver name: " + dm.getDriverName());
                logger.log(Level.INFO, "Driver version: " + dm.getDriverVersion());
                logger.log(Level.INFO, "Product name: " + dm.getDatabaseProductName());
                logger.log(Level.INFO, "Product version: " + dm.getDatabaseProductVersion());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * get the instance of this singleton connects to server defined in<br>
     * default_server_settings.conf
     *
     * @return the only instance of DataBaseConnectionManager
     * @throws SQLException
     */
    public static DatabaseConnectionManager getInstance() throws SQLException {
        if (dbObject == null) {
            dbObject = new DatabaseConnectionManager();
        }

        return dbObject;
    }

    public Connection getConnection() {
        return con;
    }

}