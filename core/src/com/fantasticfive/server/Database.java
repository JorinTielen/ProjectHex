package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.UnitType;

import java.sql.*;
import java.util.Properties;

/**
 * ProjectHex Created by Sven de Vries on 13-11-2017
 */
public class Database {
    private Connection conn;

    private void setConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "sven");
        connectionProps.put("password", "sven123");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://thedin.nl/projecthex", connectionProps);
            System.out.println("Connection to database SUCCEED");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Connection to database FAILED");
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
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?");
            myStmt.setString(1, BuildingType.BARRACKS.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                barracks = new Barracks(myRs.getInt("health"),
                        myRs.getInt("purchaseCost"),
                        getBuildableOn(BuildingType.BARRACKS.toString()));
            }

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return barracks;
    }

    public Resource getResourcePreset() {
        Resource resource = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?");
            myStmt.setString(1, BuildingType.RESOURCE.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                resource = new Resource(myRs.getInt("health"),
                        myRs.getInt("purchaseCost"),
                        myRs.getInt("productionPerTurn"),
                        getBuildableOn(BuildingType.RESOURCE.toString()));
            }

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return resource;
    }

    public Fortification getFortificationPreset() {
        Fortification fortification = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?");
            myStmt.setString(1, BuildingType.FORTIFICATION.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                fortification = new Fortification(myRs.getInt("health"),
                        myRs.getInt("purchaseCost"),
                        getBuildableOn(BuildingType.FORTIFICATION.toString()));
            }

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return fortification;
    }

    public TownCentre getTownCentrePreset() {
        TownCentre townCentre = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.building WHERE buildingType = ?");
            myStmt.setString(1, BuildingType.TOWNCENTRE.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                townCentre = new TownCentre(myRs.getInt("health"),
                        getBuildableOn(BuildingType.TOWNCENTRE.toString()));
            }

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return townCentre;
    }

    private GroundType[] getBuildableOn(String buildingType) throws SQLException {
        GroundType[] buildableOn = new GroundType[5];

        PreparedStatement myStmt = conn.prepareStatement("SELECT g.type FROM projecthex.building b, projecthex.building_groundtype bg, projecthex.groundtype g WHERE b.buildingType = bg.building_buildingType AND bg.groundType_type = g.type AND b.buildingType = ?");
        myStmt.setString(1, buildingType);
        ResultSet myRs = myStmt.executeQuery();
        int count = 0;
        while (myRs.next()) {
            buildableOn[count] = GroundType.valueOf(myRs.getString("type"));
            count++;
        }

        return buildableOn;
    }

    public Unit getSwordsmanPreset() {
        Unit swordsman = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.unit WHERE unitType = ?");
            myStmt.setString(1, UnitType.SWORDSMAN.toString());
            ResultSet myRs = myStmt.executeQuery();
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

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return swordsman;
    }

    public Unit getArcherPreset() {
        Unit archer = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.unit WHERE unitType = ?");
            myStmt.setString(1, UnitType.ARCHER.toString());
            ResultSet myRs = myStmt.executeQuery();
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

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return archer;
    }

    public Unit getScoutPreset() {
        Unit scout = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM projecthex.unit WHERE unitType = ?");
            myStmt.setString(1, UnitType.SCOUT.toString());
            ResultSet myRs = myStmt.executeQuery();
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

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return scout;
    }
}