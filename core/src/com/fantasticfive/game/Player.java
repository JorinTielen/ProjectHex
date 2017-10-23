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

        buildings = new ArrayList<Building>();
        units = new ArrayList<Unit>();
        hexagons = new ArrayList<Hexagon>();
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public Building getBuildingAtLocation(Point location) {
        for (Building building : buildings){
                if (building.getLocation().equals(location)) {
                return building;
            }
        }
        return null;
    }

    public Unit getUnitAtLocation(Point location){
        for (Unit unit : units){
            if (unit.getLocation().equals(location)) {
                return unit;
            }
        }
        return null;
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

    public void sellBuilding(Building building, int cost) {
        this.buildings.remove(building);
        this.gold = gold + (int)Math.round(cost * 0.4);
    }

    public void purchaseUnit(Unit unit) {
        if(this.gold - unit.getPurchaseCost() >= 0) {
            this.removeGold(unit.getPurchaseCost());
            this.units.add(unit);
        } else {
            System.out.println("Not enough money");
        }
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
            if(u.getSelected()) {
                u.toggleSelected();
            }
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

    public void removeUnit(Unit unit) {
        if(units.contains(unit)) {
            units.remove(unit);
        }
    }
}
