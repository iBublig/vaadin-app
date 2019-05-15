package ru.bublig.testtask.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HSQLDBConnection {

    private static volatile HSQLDBConnection instance;

    private Connection connection;
    private static final String DB_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static final String DB_URL = "jdbc:hsqldb:file:~/vaadin-app/db/testdb";
    private static final String DB_USERNAME = "SA";
    private static final String DB_PASSWORD = "";

    private HSQLDBConnection() {
        initDb();
    }

    public static HSQLDBConnection getInstance() {
        HSQLDBConnection localInstance = instance;
        if (localInstance == null) {
            synchronized (HSQLDBConnection.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new HSQLDBConnection();
                }
            }
        }
        return localInstance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initDb() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
