package com.fantasticfive.server;

import com.fantasticfive.shared.Fortification;
import com.fantasticfive.shared.Resource;
import com.fantasticfive.shared.TownCentre;
import com.fantasticfive.shared.enums.*;
import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.Barracks;
import java.sql.*;
import java.util.Properties;

/**
 * ProjectHex Created by Sven de Vries on 13-11-2017
 */
public class Database {
    private Connection conn;

    private void setConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "dbi362227");
        connectionProps.put("password", "projectHex");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://mssql.fhict.local;databaseName=dbi362227", connectionProps);
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
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM [building] WHERE [buildingType] = ?");
            myStmt.setString(1, BuildingType.BARRACKS.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                barracks = new Barracks(myRs.getInt("health"),
                        new Texture(myRs.getString("image")),
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
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM [building] WHERE [buildingType] = ?");
            myStmt.setString(1, BuildingType.RESOURCE.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                resource = new Resource(myRs.getInt("health"),
                        new Texture(myRs.getString("image")),
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
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM [building] WHERE [buildingType] = ?");
            myStmt.setString(1, BuildingType.FORTIFICATION.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                fortification = new Fortification(myRs.getInt("health"),
                        new Texture(myRs.getString("image")),
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
        TownCentre TownCentre = null;
        try {
            setConnection();
            PreparedStatement myStmt = conn.prepareStatement("SELECT * FROM [building] WHERE [buildingType] = ?");
            myStmt.setString(1, BuildingType.TOWNCENTRE.toString());
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                TownCentre = new TownCentre(myRs.getInt("health"),
                        new Texture(myRs.getString("image")),
                        getBuildableOn(BuildingType.TOWNCENTRE.toString()));
            }

            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return TownCentre;
    }

    private GroundType[] getBuildableOn(String buildingType) throws SQLException {
        GroundType[] buildableOn = new GroundType[5];

        PreparedStatement myStmt = conn.prepareStatement("SELECT g.[type] FROM [building] b, [building_groundType] bg, [groundType] g WHERE b.[buildingType] = bg.[building_buildingType] AND bg.[groundType_type] = g.[type] AND b.[buildingType] = ?");
        myStmt.setString(1, buildingType);
        ResultSet myRs = myStmt.executeQuery();
        int count = 0;
        while (myRs.next()) {
            buildableOn[count] = GroundType.valueOf(myRs.getString("type"));
            count++;
        }

        return buildableOn;
    }
}