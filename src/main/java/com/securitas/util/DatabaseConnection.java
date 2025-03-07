package com.securitas.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final Properties properties = loadProperties();
    private static Connection connection;

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Unable to find application.properties");
                throw new RuntimeException("Unable to find application.properties");
            }
            props.load(input);
            logger.debug("Successfully loaded database properties");
        } catch (IOException e) {
            logger.error("Error loading application.properties", e);
            throw new RuntimeException("Error loading application.properties", e);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            logger.debug("Attempting to establish database connection to {}", url);
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Successfully established database connection");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}
