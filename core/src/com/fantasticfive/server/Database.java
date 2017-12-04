package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.UnitType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * ProjectHex Created by Sven de Vries on 13-11-2017
 */
public class Database {
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
            System.out.println("Connection to database SUCCEED");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Connection to database FAILED");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't find properties file for database");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Error with loading properties file for database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error with driver library");
        }
    }

    private void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            conn = null;
        }
    }

    public Barracks getBarracksPreset() {
        Barracks barracks = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?")) {
            myStmt.setString(1, BuildingType.BARRACKS.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    barracks = new Barracks(myRs.getInt("health"),
                            myRs.getInt("purchaseCost"),
                            getBuildableOn(BuildingType.BARRACKS.toString()));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return barracks;
    }

    public Resource getResourcePreset() {
        Resource resource = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?")) {
            myStmt.setString(1, BuildingType.RESOURCE.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    resource = new Resource(myRs.getInt("health"),
                            myRs.getInt("purchaseCost"),
                            myRs.getInt("productionPerTurn"),
                            getBuildableOn(BuildingType.RESOURCE.toString()));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return resource;
    }

    public Fortification getFortificationPreset() {
        Fortification fortification = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?")) {
            myStmt.setString(1, BuildingType.FORTIFICATION.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    fortification = new Fortification(myRs.getInt("health"),
                            myRs.getInt("purchaseCost"),
                            getBuildableOn(BuildingType.FORTIFICATION.toString()));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return fortification;
    }

    public TownCentre getTownCentrePreset() {
        TownCentre townCentre = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?")) {
            myStmt.setString(1, BuildingType.TOWNCENTRE.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    townCentre = new TownCentre(myRs.getInt("health"),
                            getBuildableOn(BuildingType.TOWNCENTRE.toString()));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return townCentre;
    }

    private GroundType[] getBuildableOn(String buildingType) throws SQLException {
        GroundType[] buildableOn = new GroundType[5];

        try (PreparedStatement myStmt = conn.prepareStatement("SELECT g.type FROM projecthex.building b, projecthex.building_groundtype bg, projecthex.groundtype g WHERE b.buildingType = bg.building_buildingType AND bg.groundType_type = g.type AND b.buildingType = ?")) {
            myStmt.setString(1, buildingType);
            try (ResultSet myRs = myStmt.executeQuery()) {
                int count = 0;
                while (myRs.next()) {
                    buildableOn[count] = GroundType.valueOf(myRs.getString("type"));
                    count++;
                }
            }
        }

        return buildableOn;
    }

    public Unit getSwordsmanPreset() {
        Unit swordsman = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.unit WHERE unitType = ?")) {
            myStmt.setString(1, UnitType.SWORDSMAN.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    swordsman = new Unit(UnitType.SWORDSMAN,
                            myRs.getInt("health"),
                            myRs.getInt("armor"),
                            myRs.getInt("attackPower"),
                            myRs.getInt("attackRange"),
                            myRs.getInt("movementRange"),
                            myRs.getInt("purchaseCost"),
                            myRs.getInt("costPerTurn"),
                            myRs.getBoolean("canTakeLand"),
                            myRs.getInt("upgradeCost"));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return swordsman;
    }

    public Unit getArcherPreset() {
        Unit archer = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.unit WHERE unitType = ?")) {
            myStmt.setString(1, UnitType.ARCHER.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    archer = new Unit(UnitType.ARCHER,
                            myRs.getInt("health"),
                            myRs.getInt("armor"),
                            myRs.getInt("attackPower"),
                            myRs.getInt("attackRange"),
                            myRs.getInt("movementRange"),
                            myRs.getInt("purchaseCost"),
                            myRs.getInt("costPerTurn"),
                            myRs.getBoolean("canTakeLand"),
                            myRs.getInt("upgradeCost"));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return archer;
    }

    public Unit getScoutPreset() {
        Unit scout = null;
        setConnection();
        try (PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.unit WHERE unitType = ?")) {
            myStmt.setString(1, UnitType.SCOUT.toString());
            try (ResultSet myRs = myStmt.executeQuery()) {
                while (myRs.next()) {
                    scout = new Unit(UnitType.SCOUT,
                            myRs.getInt("health"),
                            myRs.getInt("armor"),
                            myRs.getInt("attackPower"),
                            myRs.getInt("attackRange"),
                            myRs.getInt("movementRange"),
                            myRs.getInt("purchaseCost"),
                            myRs.getInt("costPerTurn"),
                            myRs.getBoolean("canTakeLand"),
                            myRs.getInt("upgradeCost"));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return scout;
    }
}