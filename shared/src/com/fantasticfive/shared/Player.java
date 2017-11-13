package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player implements IPlayer{
    private List<IBuilding> buildings;
    private List<IUnit> units;
    private List<Hexagon> hexagons;
    private Color color;
    private int gold = 0;
    private String username;

    public Player(String username, Color color) {
        this.color = color;
        this.username = username;

        buildings = new ArrayList<IBuilding>();
        units = new ArrayList<IUnit>();
        hexagons = new ArrayList<Hexagon>();
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

    public void purchaseBuilding(IBuilding building) {
        //Removes gold and adds resource
        if (building instanceof Resource) {
            if (this.gold - ((Resource) building).getPurchaseCost() >= 0) {
                this.removeGold(((Resource) building).getPurchaseCost());
                this.buildings.add(building);
            } else {
                System.out.println("Not enough money");
            }
        }
        //Removes gold and adds fortification
        else if (building instanceof Fortification) {
            if (this.gold - ((Fortification) building).getPurchaseCost() >= 0) {
                this.removeGold(((Fortification) building).getPurchaseCost());
                this.buildings.add(building);
            } else {
                System.out.println("Not enough money");
            }
        }
        //Removes gold and adds barracks
        else if (building instanceof Barracks) {
            if (this.gold - ((Barracks) building).getPurchaseCost() >= 0) {
                this.removeGold(((Barracks) building).getPurchaseCost());
                this.buildings.add(building);
            } else {
                System.out.println("Not enough money");
            }
        }
        //Adds town centre
        else if (building instanceof TownCentre) {
            this.buildings.add(building);
        }
    }

    //Sells building
    public void sellBuilding(IBuilding building, int cost) {
        this.buildings.remove(building);
        this.gold += (int) Math.round(cost * 0.66);
    }

    //Removes building when destroyed
    public void removeBuilding(IBuilding building) {
        if (buildings.contains(building)) {
            buildings.remove(building);
        }
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public IBuilding getBuildingAtLocation(Point location) {
        for (Building building : buildings) {
            if (building.getLocation().equals(location)) {
                return building;
            }
        }
        return null;
    }

    public void purchaseUnit(Unit unit) {
        if (this.gold - unit.getPurchaseCost() >= 0) {
            this.removeGold(unit.getPurchaseCost());
            this.units.add(unit);
        } else {
            System.out.println("Not enough money");
        }
    }

    //Sells unit
    public void sellUnit(Unit unit) {
        if(units.contains(unit)) {
            this.addGold((int) (unit.getPurchaseCost() * 0.66));
            if (unit.getSelected()) {
                unit.toggleSelected();
            }
            this.units.remove(unit);
        }
    }

    //Removes unit when dead
    public void removeUnit(Unit unit) {
        if (units.contains(unit)) {
            units.remove(unit);
        }
    }

    public List<IUnit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    public void addHexagon(Hexagon hexagon) {
        this.hexagons.add(hexagon);
    }

    public void removeHexagon(Hexagon hexagon) {
        this.hexagons.remove(hexagon);
    }

    public void endTurn() {
        //Set unit fields back to normal
        for (IUnit u : units) {
            u.resetMoves();
            if (u.getSelected()) {
                u.toggleSelected();
            }
        }

        //Changes gold amount with gold per turn
        this.addGold(getGoldPerTurn());
    }

    public void updateResources() {
        throw new NotImplementedException();
    }

    public int getGoldPerTurn() {
        int gpt = 0;
        for (Unit u : units) {
            gpt -= u.getCostPerTurn();
        }

        for (Building b : buildings) {
            if (b instanceof Resource) {
                gpt += ((Resource) b).getProductionPerTurn();
            }
        }
        return gpt;
    }
}
