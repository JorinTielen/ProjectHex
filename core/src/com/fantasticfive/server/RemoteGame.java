package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {
    private int version = 0;
    private List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private Map map;

    private ArrayList<Color> usedColors = new ArrayList<>();
    private ArrayList<Integer> usedIds = new ArrayList<>();

    private BuildingFactory buildingFactory = new BuildingFactory();
    private UnitFactory unitFactory = new UnitFactory();

    private Registry registry;
    private static final String bindingName = "ProjectHex";

    RemoteGame(int portNumber) throws RemoteException {
        RemoteSetup(portNumber);

        map = new Map(10, 10);
    }

    private void RemoteSetup(int portNumber) {
        //Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException e) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + e.getMessage());
        }

        //Bind using registry
        try {
            registry.rebind(bindingName, this);
            System.out.println("Server: Game bound to registry");
        } catch (RemoteException e) {
            System.out.println("Server: Cannot bind Game");
            System.out.println("Server: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Server: Port already in use. \nServer: Please check if the server isn't already running");
            System.out.println("Server: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public int getVersion() throws RemoteException {
        return version;
    }

    @Override
    public void startGame() throws RemoteException {
        currentPlayer = players.get(0);
        version++;
    }

    @Override
    public void endTurn(int playerId) throws RemoteException {
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
    public void leaveGame(int playerId) throws RemoteException {
        for (Player p : players) {
            if (playerId == p.getId()) {
                endTurn(playerId);
                players.remove(p);
            }
        }

        if (players.size() == 1) {
            System.out.println(currentPlayer.getUsername() + " has won the game!");
        }
        version++;
    }

    @Override
    public void updateFromLocal(List<Player> players) throws RemoteException {
        this.players = players;
        for (Player p : players) {
            if (p.getId() == currentPlayer.getId()) {
                currentPlayer = p;
            }
        }
        version++;
    }

    //TODO: fix de locaties van town center
    @Override
    public Player addPlayer(String username) throws RemoteException {
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
        Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1, 0), p);
        p.purchaseBuilding(b);
        p.addHexagon(map.getHexAtLocation(new Point(1, 0)));
        p.addHexagon(map.getHexAtLocation(new Point(1, 1)));
        p.addHexagon(map.getHexAtLocation(new Point(0, 0)));
        p.addHexagon(map.getHexAtLocation(new Point(0, 1)));
        p.addHexagon(map.getHexAtLocation(new Point(2, 1)));
        p.addHexagon(map.getHexAtLocation(new Point(2, 0)));

        usedColors.add(color);
        usedIds.add(id);

        players.add(p);
        startGame();
        version++;
        return p;
    }

    @Override
    public List<Player> getPlayers() throws RemoteException {
        return players;
    }

    @Override
    public Player getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }

    private void removePlayer(Player player) throws RemoteException {
        this.players.remove(player);

        if (players.size() == 1) {
            System.out.println(currentPlayer.getUsername() + " has won the game!");
        }
        version++;
    }

    @Override
    public Unit getUnitPreset(UnitType type) throws RemoteException {
        return unitFactory.getUnitPreset(type);
    }

    @Override
    public void buyUnit(UnitType unitType, Point location, int playerId) throws RemoteException {
        if (playerId == currentPlayer.getId()) {
            if (hexEmpty(location)) {
                Unit unit = unitFactory.getUnitPreset(unitType);
                //When player has enough gold to buy unit
                currentPlayer.purchaseUnit(unitFactory.createUnit(unitType, location, currentPlayer));
            }
            //When hex is not empty
            else {
                System.out.println("Hex not empty");
            }
        }
        version++;
    }

    @Override
    public void claimLand(Unit unit) throws RemoteException {
        if (unit.getUnitType() == UnitType.SCOUT) {
            Hexagon h = map.getHexAtLocation(unit.getLocation());
            if (!h.hasOwner() && map.bordersOwnLand(unit.getLocation(), currentPlayer)) {
                h.setOwner(currentPlayer);
                currentPlayer.addHexagon(h);
                claimMountains(unit.getLocation());
            }
        }
    }

    //Checks if claimed land has any mountain next to it, if it does it claims it.
    private void claimMountains(Point location){
        Hexagon h = map.getHexAtLocation(new Point(location.x - 1, location.y - 1));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x - 1, location.y ));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x - 1, location.y + 1));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x, location.y - 1));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x, location.y + 1));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x + 1, location.y - 1));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x + 1, location.y));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
        h = map.getHexAtLocation(new Point(location.x + 1, location.y + 1));
        if (!h.hasOwner() && h.getIsMountain()){
            currentPlayer.addHexagon(h);
        }
    }

    @Override
    public Building getBuildingPreset(BuildingType type) throws RemoteException {
        return buildingFactory.getBuildingPreset(type);
    }

    public void buyBuilding(BuildingType buildingType, Point location) throws RemoteException {
        if (map.isHexBuildable(location, currentPlayer)) {
            if (buildingType == BuildingType.RESOURCE && hexEmptyResource(location)) {
                if (currentPlayer.purchaseBuilding(buildingFactory.createBuilding(buildingType, location, currentPlayer), true)) {
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
    public void sellBuilding(Point location) throws RemoteException {
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
            if (map.getHexAtLocation(location).getIsMountain()){
                map.getHexAtLocation(location).setMountain();
            }
            currentPlayer.sellBuilding(building, cost);
        }
        version++;
    }

    @Override
    public void attackBuilding(Unit unit, Point location) throws RemoteException {
        Building building = getBuildingAtLocation(location);
        if (unit != null && building != null) {
            if (unit.attack(building)) {
                Player enemy = building.getOwner();
                enemy.removeBuilding(building);
                if (building instanceof TownCentre) {
                    removePlayer(enemy);
                }
            }
        }
    }

    @Override
    public Building getBuildingAtLocation(Point location) throws RemoteException {
        for (Player player : players) {
            Building building = player.getBuildingAtLocation(location);
            if (building != null) {
                return building;
            }

        }
        return null;
    }

    @Override
    public Map getMap() throws RemoteException {
        return map;
    }

    @Override
    public boolean hexEmpty(Point location) throws RemoteException {
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
        if (hex.getObjectType() == ObjectType.MOUNTAIN
                || hex.getGroundType() == GroundType.WATER
                || getBuildingAtLocation(location) != null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hexEmptyResource(Point location) throws RemoteException {
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
        if (hex.getObjectType() == ObjectType.MOUNTAIN
                && getBuildingAtLocation(location) == null
                && hex.getGroundType() != GroundType.WATER) {
            return true;
        }
        return false;
    }

    public Unit getUnitOnHex(Hexagon hex) throws RemoteException {
        Unit unit = null;
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                if (u.getLocation().x == hex.getLocation().x && u.getLocation().y == hex.getLocation().y) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public Unit getSelectedUnit() throws RemoteException {
        Unit unit = null;
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                if (u.getSelected()) {
                    unit = u;
                }
            }
        }
        return unit;
    }
}
