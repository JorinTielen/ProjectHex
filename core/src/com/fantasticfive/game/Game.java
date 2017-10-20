package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.Color;
import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<Player>();
    private Map map;
    private UnitFactory unitFactory = new UnitFactory();
    private BuildingFactory buildingFactory = new BuildingFactory();
    private String hash;
    private int id;

    public Game() {
    }

    public void addPlayer(String username) {
        boolean available = true;
        ArrayList<Color> usedColors = new ArrayList<Color>();

        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                System.out.println("This username is already in this game!");
                available = false;
            }
            if (!usedColors.contains(p.getColor())) {
                usedColors.add(p.getColor());
            }
        }

        Color color;
        do {
            color = Color.getRandomColor();
        } while (usedColors.contains(color));

        if (available) {
            Player p = new Player(username, color);
            players.add(p);
            Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1, 0), p); //Random Point???
            p.purchaseBuilding(b);
            Unit u = unitFactory.createUnit(UnitType.SWORDSMAN, new Point(2,0), p);
            p.purchaseUnit(u);
        }
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void startGame() {
        throw new NotImplementedException();
    }

    public void generateHash() {
        throw new NotImplementedException();
    }

    public void createUnit(Player player, UnitType unitType, Point location) {
        if (hexEmpty(location)) {
            Unit unit = unitFactory.getUnitPreset(unitType);
            if (player.getGold() - unit.getPurchaseCost() > 0) {
                player.purchaseUnit(unitFactory.createUnit(unitType, location, player));
            } else {
                throw new NotImplementedException();
            }
        }
    }

    public void createBuilding(Player player, BuildingType buildingType, Point location) {
        if (map.isHexBuildable(location, player)) {
            player.purchaseBuilding(buildingFactory.createBuilding(buildingType, location, player));
            map.createBuilding(buildingType, location);
        }
    }

    public void claimLand(Unit unit) {
        throw new NotImplementedException();
    }

    public void update() {
        throw new NotImplementedException();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void MoveUnit(Unit unit, Point location) {
        if (hexEmpty(location)) {
            unit.move(location);
        }
    }

    public boolean hexEmpty(Point location) {
        boolean empty = true;
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                if (unit.getLocation() == location) {
                    empty = false;
                }
            }
        }
        return empty;
    }

    public Unit getUnitOnHex(Hexagon hex) {
        Unit unit = null;
        for(Player p: getPlayers()) {
            for(Unit u: p.getUnits()) {
                if (u.getLocation().x == hex.getLocation().x && u.getLocation().y == hex.getLocation().y) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public Unit getSelectedUnit() {
        Unit unit = null;
        //TODO implement current player only
        for(Player p: getPlayers()) {
            for(Unit u: p.getUnits()) {
                if(u.getSelected()) {
                    unit = u;
                }
            }
        }
        return unit;
    }
}
