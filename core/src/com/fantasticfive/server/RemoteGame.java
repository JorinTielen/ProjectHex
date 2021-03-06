package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.*;
import fontyspublisher.RemotePublisher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {
    private static final Logger LOGGER = Logger.getLogger(RemoteGame.class.getName());

    private int version = 0;
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

    private RemotePublisher publisher;
    private static final String publisherBindingName = "Publisher";

    public RemoteGame(int portNumber) throws RemoteException {
        RemoteSetup(portNumber);
        map = new Map(20, 10);
    }

    private void RemoteSetup(int portNumber) {
        //Create publisher
        try {
            publisher = new RemotePublisher();
            publisher.registerProperty("Players");
        } catch (RemoteException e) {
            LOGGER.severe("Server: Cannot create publisher");
            LOGGER.severe("Server: RemoteException " + e.getMessage());
        }

        //Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            LOGGER.info("Server: Registry created on port number " + portNumber);
        } catch (RemoteException e) {
            LOGGER.severe("Server: Cannot create registry");
            LOGGER.severe("Server: RemoteException: " + e.getMessage());
        }

        //Bind using registry
        try {
            registry.rebind(bindingName, this);
            registry.rebind(publisherBindingName, publisher);
            LOGGER.info("Server: Game bound to registry");
        } catch (RemoteException e) {
            LOGGER.severe("Server: Cannot bind Game");
            LOGGER.severe("Server: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            LOGGER.severe("Server: Port already in use. \nServer: Please check if the server isn't already running");
            LOGGER.severe("Server: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void startGame() {
        currentPlayer = players.get(0);

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    @Override
    public void endTurn(int playerId) {
        if (players.size() != 0) {
            if (currentPlayer.getId() == playerId) {
                if (currentPlayer.getTurnsWithoutGold() == 3) {
                    leaveGame(currentPlayer.getId());
                } else {
                    currentPlayer.endTurn();
                }
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

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    @Override
    public void leaveGame(int playerId) {
        for (Player p : players) {
            if (playerId == p.getId()) {
                if (playerId == currentPlayer.getId() && currentPlayer.getTurnsWithoutGold() < 3) {
                    endTurn(playerId);
                }
                players.remove(p);
                break;
            }
        }

        if (players.size() == 1) {
            LOGGER.info(currentPlayer.getUsername() + " has won the game!");
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    @Override
    public void updateFromLocal(List<Player> players) {
        this.players = players;
        for (Player p : players) {
            if (p.getId() == currentPlayer.getId()) {
                currentPlayer = p;
            }
        }

        try {
            publisher.inform("Players", players, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
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
        int distance;
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
        } while (distance < 4 || !hexEmpty(location) || location.getY() == 0 || location.getY() == map.getHeight() - 1
                || location.getX() == 0 || location.getX() == map.getWidth() - 1);
        Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, location, p);
        p.purchaseBuilding(b);

        List<Hexagon> ownedLand = map.getHexesInRadius(b.getLocation(), 1);
        for (Hexagon hex : ownedLand) {
            if (hex.getGroundType() != GroundType.WATER) {
                p.addHexagon(hex);
            }
        }

        usedColors.add(color);
        usedIds.add(id);

        players.add(p);
        startGame();

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }

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

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    @Override
    public Unit getUnitPreset(UnitType type) {
        return unitFactory.getUnitPreset(type);
    }

    public Unit getUnitAtLocation(Point location) {
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                if (u.getLocation().sameAs(location)) {
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public Hexagon buyUnit(UnitType unitType, Point location, int playerId) {
        Hexagon hexagon = null;
        if (playerId == currentPlayer.getId()) {
            //When player has enough gold to buy unit
            boolean tileEmpty = false;

            for (Hexagon hex : map.getHexesInRadius(location, 1)) {
                if (hexEmpty(hex.getLocation())) {
                    tileEmpty = true;
                    currentPlayer.purchaseUnit(unitFactory.createUnit(unitType, hex.getLocation(), currentPlayer));
                    hexagon = hex;
                    break;
                }
            }

            if (!tileEmpty) {
                LOGGER.info("no tiles empty!");
            }
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
        return hexagon;
    }


    @Override
    public boolean moveUnit(Unit u, Point location, int playerId, List<Hexagon> walkableHexes, int distance) {
        if (playerId == currentPlayer.getId()) {
            if (map.canMoveTo(u, location, walkableHexes) &&
                    getBuildingAtLocation(location) == null &&
                    getUnitAtLocation(location) == null &&
                    map.getHexAtLocation(location).getGroundType() != GroundType.WATER &&
                    map.getHexAtLocation(location).getObjectType() != ObjectType.MOUNTAIN) {
                Unit realUnit = getUnitAtLocation(u.getLocation());
                realUnit.move(location, distance);

                try {
                    publisher.inform("Players", null, players);
                    version++;
                } catch (RemoteException e) {
                    LOGGER.log(Level.ALL, e.getMessage());
                }

                return true;
            }
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }

        return false;
    }

    @Override
    public int attackUnit(Unit attacker, Unit defender) {
        int result = 0;
        if (map.isWithinAttackRange(attacker, defender.getLocation())) {
            Unit realAttacker = getUnitAtLocation(attacker.getLocation());
            Unit realDefender = getUnitAtLocation(defender.getLocation());
            int beginHP = realDefender.getHealth();
            realAttacker.attack(realDefender);
            int endHP = realDefender.getHealth();
            result = beginHP - endHP;

            version++;
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }

        return result;
    }

    @Override
    public void sellUnit(Unit u) {
        for (Player p : players) {
            for (Unit unit : p.getUnits()) {
                if (unit.getLocation().sameAs(u.getLocation())) {
                    p.sellUnit(unit);

                    try {
                        publisher.inform("Players", null, players);
                        version++;
                    } catch (RemoteException e) {
                        LOGGER.log(Level.ALL, e.getMessage());
                    }

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

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    //Checks if claimed land has any mountain next to it, if it does it claims it.
    private void claimMountains(Point location) {
        for (Hexagon h : map.getHexesInRadius(location, 1)) {
            if (!h.hasOwner() && h.getIsMountain()) {
                currentPlayer.addHexagon(h);
            }
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    @Override
    public Building getBuildingPreset(BuildingType type) {
        return buildingFactory.getBuildingPreset(type);
    }

    public void buyBuilding(BuildingType buildingType, Point location) {
        if (buildingType == BuildingType.RESOURCE && hexEmptyResource(location) && currentPlayerOwnsLand(location)) {
            currentPlayer.purchaseBuildingOnMountain(buildingFactory.createBuilding(buildingType, location, currentPlayer));
            if(currentPlayer.getBuildingAtLocation(location) != null) {
                currentPlayer.getBuildingAtLocation(location).setResourceOnMountain(true);
            }
        }
        else if (hexEmpty(location) && currentPlayerOwnsLand(location)) {
            currentPlayer.purchaseBuilding(buildingFactory.createBuilding(buildingType, location, currentPlayer));
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
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
                building.setResourceOnMountain(false);
            }
            currentPlayer.sellBuilding(building, cost);
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    @Override
    public void attackBuilding(Unit unit, Building building) {
        if (unit != null && building != null) {
            Unit realUnit = getUnitAtLocation(unit.getLocation());
            Building realBuilding = getBuildingAtLocation(building.getLocation());

            if (map.isWithinAttackRange(realUnit, realBuilding.getLocation())) {
                if (realUnit.attack(realBuilding)) {
                    Player enemy = realBuilding.getOwner();
                    enemy.destroyBuilding(realBuilding);
                    if (building instanceof TownCentre) {
                        removePlayer(enemy);
                    }
                }
            }
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
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

    public void destroyBuilding(Building building) {
        Building realBuilding = getBuildingAtLocation(building.getLocation());
        if (realBuilding != null) {
            realBuilding.getOwner().removeBuilding(realBuilding);
        }

        try {
            publisher.inform("Players", null, players);
            version++;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
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
                if (unit.getLocation().sameAs(location)) {
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
                if (unit.getLocation().sameAs(location)) {
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

    private boolean currentPlayerOwnsLand(Point location) {
        //Check if currentPlayer owns land
        if (map.getHexAtLocation(location).getOwner() != currentPlayer) {
            return false;
        }
        return true;
    }

    public boolean lastPlayer() {
        if (players.size() > 1) {
            gameHasHadMultiplePlayers = true;
        }
        return (gameHasHadMultiplePlayers && players.size() == 1);
    }
}
