package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Player implements Serializable {
    private static final Logger LOGGER = Logger.getLogger( Player.class.getName() );

    private List<Building> buildings;
    private List<Unit> units;
    private List<Hexagon> hexagons;
    private Color color;
    private int gold;
    private String username;
    private int id;

    public Player(String username, Color color, int id) {
        this.color = color;
        this.username = username;

        buildings = new ArrayList<>();
        units = new ArrayList<>();
        hexagons = new ArrayList<>();
        this.gold = 100;

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Hexagon> getOwnedHexagons() {
        return hexagons;
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
        //Removes gold and adds resource
        if (building instanceof Resource) {
            if (this.gold - ((Resource) building).getPurchaseCost() >= 0) {
                this.removeGold(((Resource) building).getPurchaseCost());
                this.buildings.add(building);
                LOGGER.info("Resource built");
            } else {
                LOGGER.info("Not enough money");
            }
        }
        //Removes gold and adds fortification
        else if (building instanceof Fortification) {
            if (this.gold - ((Fortification) building).getPurchaseCost() >= 0) {
                this.removeGold(((Fortification) building).getPurchaseCost());
                this.buildings.add(building);
                LOGGER.info("Fortification built");
            } else {
                LOGGER.info("Not enough money");
            }
        }
        //Removes gold and adds barracks
        else if (building instanceof Barracks) {
            if (this.gold - ((Barracks) building).getPurchaseCost() >= 0) {
                this.removeGold(((Barracks) building).getPurchaseCost());
                this.buildings.add(building);
                LOGGER.info("Barracks built");
            } else {
                LOGGER.info("Not enough money");
            }
        }
        //Adds town centre
        else if (building instanceof TownCentre) {
            this.buildings.add(building);
            LOGGER.info("TownCentre built");
        }
    }

    public boolean purchaseBuildingOnMountain(Building building){
        if (this.gold - ((Resource) building).getPurchaseCost() >= 0) {
            this.removeGold(((Resource) building).getPurchaseCost());
            building.setMineOnMountain();
            this.buildings.add(building);
            LOGGER.info("Resource on mountain built");
            return true;
        } else {
            LOGGER.info("Not enough money");
            return false;
        }
    }

    //Sells building
    public void sellBuilding(Building building, int cost) {
        this.buildings.remove(building);
        this.gold += (int) Math.round(cost * 0.66);
    }

    //Removes building when destroyed
    public void removeBuilding(Building building) {
        if (buildings.contains(building)) {
            buildings.remove(building);
        }
    }

    public void destroyBuilding(Building building){
        building.destroy();
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public Building getBuildingAtLocation(Point location) {
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
            LOGGER.info("Not enough money");
        }
    }

    //Sells unit
    public void sellUnit(Unit unit) {
        if (units.contains(unit)) {
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

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    public void addHexagon(Hexagon hexagon) {
        this.hexagons.add(hexagon);
        hexagon.setOwner(this);
    }

    public void removeHexagon(Hexagon hexagon) {
        this.hexagons.remove(hexagon);
    }

    public void endTurn() {
        //Set unit fields back to normal
        for (Unit u : units) {
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