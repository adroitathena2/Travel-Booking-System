package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/tbs";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = ""; //Enter your SQL root password here

    public static Connection getConnection() throws SQLException {
        try {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream("config/db.properties")) {
                props.load(in);
            }
            
            String url = props.getProperty("db.url", DEFAULT_URL);
            String user = props.getProperty("db.user", DEFAULT_USER);
            String password = props.getProperty("db.password", DEFAULT_PASSWORD);
            
            return DriverManager.getConnection(url, user, password);
        } catch (IOException e) { 
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");
            
            if (url == null) url = DEFAULT_URL;
            if (user == null) user = DEFAULT_USER;
            if (password == null) password = DEFAULT_PASSWORD;
            
            return DriverManager.getConnection(url, user, password);
        }
    }
}