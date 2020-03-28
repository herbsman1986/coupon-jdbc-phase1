package pool;

import io.vavr.control.Try;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ConnectionPool {

    // Class attributes
    private Set<Connection> connections;

    // Create one instance of an object
    private static ConnectionPool instance = null;

    // URL for connections
    private static final int MAX_CONNECTION = 15;
    private static final String URL = "jdbc:mysql://localhost:3306/coupon" + "?user=root"
            + "&password=root" + "&useUnicode=true" + "&useJDBCCompliantTimezoneShift=true"
            + "&useLegacyDatetimeCode=false" + "&serverTimezone=UTC";

    /***
     * Private CTR
     */
    private ConnectionPool() {
        connections = new HashSet<>();
        for (int i = 0; i < MAX_CONNECTION; i++) {
            try {
                Connection connection = DriverManager.getConnection(URL);
                connections.add(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Get connection from the set in order to use
     * @return
     * @throws SQLException
     */
    public synchronized Connection getConnection() {
        while (connections.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Some one interrupted waiting");
            }
        }
        Iterator<Connection> it = connections.iterator();
        Connection connection = it.next();
        it.remove();
        return connection;
    }

    /***
     * Returns Connection to set after usage
     * @param connection
     */
    public synchronized void returnConnection(Connection connection) {
        connections.add(connection);
        notifyAll();
    }

    /***
     * Close all connections
     */
    public synchronized void closeAllConnections() {
        // Counter for checking if all connections are close
        int counter = 0;

        // Checking if the remove-counter less the max connections
        while (counter < MAX_CONNECTION) {

            // While its empty wait..
            while (connections.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Someone interrupt waiting");
                }
            }
            // Running over the available connections
            // Closing the connection and adding 1 to the counter
            Iterator<Connection> itCon = connections.iterator();
            while (itCon.hasNext()) {
                Connection currentConnection = itCon.next();

                try {
                    currentConnection.close();
                    counter++;
                } catch (SQLException e) {
                    System.err.println("Couldnt close the current connection");
                }
            }
        }
    }

    /***
     * Get the one connection pool object
     *
     * @return
     */
    public synchronized static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }
}


