package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {
    private static final Logger LOGGER = Logger.getLogger(RemoteGame.class.getName());

    private int version = 0;
    private boolean ready;
    private List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private Map map;

    private ArrayList<Color> usedColors = new ArrayList<>();
    private ArrayList<Integer> usedIds = new ArrayList<>();

    private BuildingFactory buildingFactory = new BuildingFactory();
    private UnitFactory unitFactory = new UnitFactory();

    private boolean gameHasHadMultiplePlayers = false;

    private Registry registry;
    private static final String bindingName = "ProjectHex";

    public RemoteGame(int portNumber) throws RemoteException {
        RemoteSetup(portNumber);
        map = new Map(20, 10);
        ready = true;
    }

    private void RemoteSetup(int portNumber) {
        //Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            LOGGER.info("Server: Registry created on port number " + portNumber);
        } catch (RemoteException e) {
            LOGGER.info("Server: Cannot create registry");
            LOGGER.info("Server: RemoteException: " + e.getMessage());
        }

        //Bind using registry
        try {
            registry.rebind(bindingName, this);
            LOGGER.info("Server: Game bound to registry");
        } catch (RemoteException e) {
            LOGGER.info("Server: Cannot bind Game");
            LOGGER.info("Server: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            LOGGER.info("Server: Port already in use. \nServer: Please check if the server isn't already running");
            LOGGER.info("Server: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void startGame() {
        currentPlayer = players.get(0);
        version++;
    }

    @Override
    public void endTurn(int playerId) {
        if (players.size() != 0) {
            if (currentPlayer.getId() == playerId) {
                currentPlayer.endTurn();
                int i = players.indexOf(currentPlayer);

                //If current player is not the last in the list
                if (i != players.size() - 1) {
                    currentPlayer = players.get(i + 1);
                }
                //If current player is the last in the list
                else {
                    currentPlayer = players.get(0);
                }
            }
        }
        version++;
    }

    @Override
    public void leaveGame(int playerId) {
        for (Player p : players) {
            if (playerId == p.getId()) {
                endTurn(playerId);
                players.remove(p);
                break;
            }
        }

        if (players.size() == 1) {
            LOGGER.info(currentPlayer.getUsername() + " has won the game!");
        }
        version++;
    }

    @Override
    public void updateFromLocal(List<Player> players) {
        this.players = players;
        for (Player p : players) {
            if (p.getId() == currentPlayer.getId()) {
                currentPlayer = p;
            }
        }
        version++;
    }

    @Override
    public Player addPlayer(String username) {
        Color color;
        do {
            color = Color.getRandomColor();
        } while (usedColors.contains(color));

        Integer id;
        do {
            Random rnd = new Random();
            id = rnd.nextInt(1000);
        } while (usedIds.contains(id));

        Player p = new Player(username, color, id);

        Point location;
        int distance = 999;
        do {
            location = map.randomPoint();
            distance = 999;
            for (Player otherPlayer : players) {
                Point townCenter = otherPlayer.getBuildings().get(0).getLocation();
                int townCenter_distance = map.distance(location, townCenter);
                if (townCenter_distance < distance) {
                    distance = townCenter_distance;
                }
            }
        } while (distance < 4 || !hexEmpty(location));
        Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, location, p);
        p.purchaseBuilding(b);

        List<Hexagon> ownedLand = map.hexesInCirle(b.getLocation(), 1);
        for (Hexagon hex : ownedLand) {
            p.addHexagon(hex);
        }

        usedColors.add(color);
        usedIds.add(id);

        players.add(p);
        startGame();
        version++;
        return p;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void removePlayer(Player player) {
        this.players.remove(player);

        if (players.size() == 1) {
            LOGGER.info(currentPlayer.getUsername() + " has won the game!");
        }
        version++;
    }

    @Override
    public Unit getUnitPreset(UnitType type) {
        return unitFactory.getUnitPreset(type);
    }

    public Unit getUnitAtLocation(Point location) {
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                if (u.getLocation().equals(location)) {
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public void buyUnit(UnitType unitType, Point location, int playerId) {
        if (playerId == currentPlayer.getId()) {
            if (hexEmpty(location)) {
                //When player has enough gold to buy unit
                currentPlayer.purchaseUnit(unitFactory.createUnit(unitType, location, currentPlayer));
            }
            //When hex is not empty
            else {
                LOGGER.info("Hex not empty");
            }
        }
        version++;
    }

    @Override
    public boolean moveUnit(Unit u, Point location, int playerId) {
        if (playerId == currentPlayer.getId()) {
            if (map.canMoveTo(u, location) &&
                    getBuildingAtLocation(location) == null &&
                    getUnitAtLocation(location) == null &&
                    map.getHexAtLocation(location).getGroundType() != GroundType.WATER &&
                    map.getHexAtLocation(location).getObjectType() != ObjectType.MOUNTAIN) {
                Unit realUnit = getUnitAtLocation(u.getLocation());
                realUnit.move(location, map.distance(u.getLocation(), location));
                version++;
                return true;
            }
        }
        version++;
        return false;
    }

    @Override
    public void attackUnit(Unit attacker, Unit defender) {
        if (map.isWithinAttackRange(attacker, defender.getLocation())) {
            Unit realAttacker = getUnitAtLocation(attacker.getLocation());
            Unit realDefender = getUnitAtLocation(defender.getLocation());
            realAttacker.attack(realDefender);
        }
        version++;
    }

    @Override
    public void sellUnit(Unit u) {
        for (Player p : players) {
            for (Unit unit : p.getUnits()) {
                if (unit.getLocation().equals(u.getLocation())) {
                    p.sellUnit(unit);
                    version++;
                    return;
                }
            }
        }
    }

    @Override
    public void claimLand(Unit unit) {
        if (unit.getUnitType() == UnitType.SCOUT) {
            Hexagon h = map.getHexAtLocation(unit.getLocation());
            if (!h.hasOwner() && map.bordersOwnLand(unit.getLocation(), currentPlayer)) {
                h.setOwner(currentPlayer);
                currentPlayer.addHexagon(h);
                claimMountains(unit.getLocation());
                Unit realUnit = getUnitAtLocation(unit.getLocation());
                realUnit.claimedLand();
            }
        }
        version++;
    }

    //Checks if claimed land has any mountain next to it, if it does it claims it.
    private void claimMountains(Point location) {
        for (Hexagon h : map.hexesInCirle(location, 1)) {
            if (!h.hasOwner() && h.getIsMountain()) {
                currentPlayer.addHexagon(h);
            }
        }
        version++;
    }

    @Override
    public Building getBuildingPreset(BuildingType type) {
        return buildingFactory.getBuildingPreset(type);
    }

    public void buyBuilding(BuildingType buildingType, Point location) {
        if (map.isHexBuildable(location, currentPlayer)) {
            if (buildingType == BuildingType.RESOURCE && hexEmptyResource(location)) {
                if (currentPlayer.purchaseBuildingOnMountain(buildingFactory.createBuilding(buildingType, location, currentPlayer))) {
                    map.getHexAtLocation(location).removeObjectType();
                    map.getHexAtLocation(location).removeObject();
                }
            } else if (hexEmpty(location)) {
                currentPlayer.purchaseBuilding(buildingFactory.createBuilding(buildingType, location, currentPlayer));
            }
        }
        version++;
    }

    @Override
    public void sellBuilding(Point location) {
        Building building = currentPlayer.getBuildingAtLocation(location);
        if (building != null && !(building instanceof TownCentre)) {
            int cost = 0;
            if (building instanceof Barracks) {
                cost = ((Barracks) buildingFactory.getBuildingPreset(BuildingType.BARRACKS)).getPurchaseCost();
            } else if (building instanceof Fortification) {
                cost = ((Fortification) buildingFactory.getBuildingPreset(BuildingType.FORTIFICATION)).getPurchaseCost();
            } else if (building instanceof Resource) {
                cost = ((Resource) buildingFactory.getBuildingPreset(BuildingType.RESOURCE)).getPurchaseCost();
            }
            if (map.getHexAtLocation(location).getIsMountain()) {
                map.getHexAtLocation(location).setMountain();
            }
            currentPlayer.sellBuilding(building, cost);
        }
        version++;
    }

    @Override
    public void attackBuilding(Unit unit, Building building) {
        if (unit != null && building != null) {
            if (map.isWithinAttackRange(unit, building.getLocation())) {
                if (unit.attack(building)) {
                    Player enemy = building.getOwner();
                    enemy.destroyBuilding(building);
                    //enemy.removeBuilding(building); //TODO test if can be deleted
                    if (building instanceof TownCentre) {
                        enemy.destroyBuilding(building);
                        removePlayer(enemy);
                    }
                }
            }
        }
        version++;
    }

    @Override
    public Building getBuildingAtLocation(Point location) {
        for (Player player : players) {
            Building building = player.getBuildingAtLocation(location);
            if (building != null) {
                return building;
            }

        }
        return null;
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public boolean hexEmpty(Point location) {
        //Check if unit is on hex
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                if (unit.getLocation().equals(location)) {
                    return false;
                }
            }
        }

        //Check if hex isn't water, mountain or possessed by a building
        Hexagon hex = map.getHexAtLocation(location);
        return (hex.getObjectType() != ObjectType.MOUNTAIN
                && hex.getGroundType() != GroundType.WATER
                && getBuildingAtLocation(location) == null);
    }

    private boolean hexEmptyResource(Point location) {
        //Check if unit is on hex
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                if (unit.getLocation().equals(location)) {
                    return false;
                }
            }
        }

        //Check if hex is a mountain for resource
        Hexagon hex = map.getHexAtLocation(location);
        return (hex.getObjectType() == ObjectType.MOUNTAIN
                && getBuildingAtLocation(location) == null
                && hex.getGroundType() != GroundType.WATER);
    }

    public boolean lastPlayer() throws RemoteException {
        if(players.size() > 1) {
            gameHasHadMultiplePlayers = true;
        }
        if(gameHasHadMultiplePlayers) {
            if (players.size() == 1) {
                return true;
            }
        }
        return false;
    }
}
