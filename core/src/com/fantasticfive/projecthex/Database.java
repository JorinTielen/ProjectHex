package com.fantasticfive.projecthex;

import com.fantasticfive.shared.Barracks;
import com.fantasticfive.shared.enums.BuildingType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(com.fantasticfive.server.Database.class.getName());

    private Connection conn;

    private void setConnection() {
        Properties prop = new Properties();
        InputStream input;

        try {
            input = new FileInputStream("database.properties");
            prop.load(input);

            String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            LOGGER.info("Client: Connection to database SUCCEEDED");
        } catch (ClassNotFoundException ex) {
            LOGGER.info(ex.getMessage());
            LOGGER.info("Client: Connection to database FAILED");
        } catch (FileNotFoundException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info("Client: Couldn't find properties file for database");
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info("Client: Error with loading properties file for database");
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info("Client: Error with driver library");
        }
    }

    private void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            LOGGER.info("Client: " + ex.getMessage());
        } finally {
            conn = null;
        }
    }

    public boolean checkUsername(String username) {
        boolean userExists = false;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.account WHERE username = ?")) {
            myStmt.setString(1, username);
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    userExists = true;
                }
            }
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        } finally {
            closeConnection();
        }
        return userExists;
    }

    public boolean checkLogin(String username, String password) {
        boolean loginOk = false;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.account WHERE username = ? AND password = ?")) {
            myStmt.setString(1, username);
            myStmt.setString(2, password);
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    loginOk = true;
                }
            }
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        } finally {
            closeConnection();
        }
        return loginOk;
    }

    public boolean registerAccount(String username, String password) {
        int registerOk = 0;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("INSERT INTO projecthex.account (username, password) VALUES (?, ?)")) {
            myStmt.setString(1, username);
            myStmt.setString(2, password);
            registerOk = myStmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        } finally {
            closeConnection();
        }
        return registerOk == 1;
    }

    public void removeAccount(String username) {
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("DELETE FROM projecthex.account WHERE username = ?")) {
            myStmt.setString(1, username);
            myStmt.executeQuery();
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        } finally {
            closeConnection();
        }
    }
}
