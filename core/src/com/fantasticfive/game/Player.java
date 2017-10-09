package com.fantasticfive.game;

import com.fantasticfive.game.enums.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private List<Building> buildings;
    private List<Unit> units = new ArrayList<Unit>();
    private List<Hexagon> hexagons;
    private Color color;
    private int gold = 0;
    private String username;

    public Player(String username, Color color) {
        this.color = color;
        this.username = username;
    }

    public Color getColor() {
        return this.color;
    }

    public String getUsername() {
        return this.username;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void removeGold(int gold) {
        this.gold -= gold;
    }

    public void purchaseBuilding(Building building) {
        this.buildings.add(building); //still need to remove gold
    }

    public void sellBuilding(Building building) {
        this.buildings.remove(building); //still need to remove gold
    }

    public void purchaseUnit(Unit unit) {
        this.removeGold(unit.getPurchaseCost());
        this.units.add(unit);
        System.out.println("Aantal units in units: " + getUnits().size());
    }

    public void sellUnit(Unit unit) {
        this.addGold((int)(unit.getPurchaseCost() * 0.66));
        this.units.remove(unit);
    }

    public void addHexagon(Hexagon hexagon) {
        this.hexagons.add(hexagon);
    }

    public void removeHexagon(Hexagon hexagon) {
        this.hexagons.remove(hexagon);
    }

    public void endTurn() {
        for(Unit u: units) {
            u.resetMoves();
            this.removeGold(u.getCostPerTurn());
        }
    }

    public void leaveGame() {
        throw new NotImplementedException();
    }

    public void updateResources() {
        throw new NotImplementedException();
    }

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }
}
