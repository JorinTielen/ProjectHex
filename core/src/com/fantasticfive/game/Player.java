package com.fantasticfive.game;

import javafx.scene.paint.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class Player {
    private List<Building> buildings;
    private List<Unit> units;
    private List<Hexagon> hexagons;
    private Color color;
    private int gold = 0;
    private String username;

    public Player(String username, Color color) {
        this.color = color;
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void removeGold(int gold) {
        this.gold += gold;
    }

    public void purchaseBuilding(Building building) {
        this.buildings.add(building); //still need to remove gold
    }

    public void sellBuilding(Building building) {
        this.buildings.remove(building); //still need to remove gold
    }

    public void purchaseUnit(Unit unit) {
        this.units.add(unit); //still need to remove gold
    }

    public void sellUnit(Unit unit) {
        this.units.remove(unit); //still need to remove gold
    }

    public void addHexagon(Hexagon hexagon) {
        this.hexagons.add(hexagon);
    }

    public void removeHexagon(Hexagon hexagon) {
        this.hexagons.remove(hexagon);
    }

    public void endTurn() {
        throw new NotImplementedException();
    }

    public void leaveGame() {
        throw new NotImplementedException();
    }

    public void updateResources() {
        throw new NotImplementedException();
    }
}
