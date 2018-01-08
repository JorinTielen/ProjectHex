package com.fantasticfive.projecthex;

import com.badlogic.gdx.Gdx;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.ObjectType;
import com.fantasticfive.shared.enums.UnitType;
import fontyspublisher.IPropertyListener;
import fontyspublisher.IRemotePropertyListener;
import fontyspublisher.IRemotePublisherForListener;
import fontyspublisher.RemotePublisher;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalGame {

    private static final Logger LOGGER = Logger.getLogger(LocalGame.class.getName());

    private IRemoteGame remoteGame;

    private List<Player> players = new ArrayList<>();
    private Player thisPlayer;
    private Map map;
    private Fog fog;

    private HashMap lastPathGenerated;

    private FontysListener fontysListener;

    public void UpdateFromRemotePush(List<Player> remotePlayers) {
        Gdx.app.postRunnable(() -> {
            players = remotePlayers;

            for (Player p : players) {
                if (thisPlayer.getId() == p.getId()) {
                    thisPlayer = p;
                }
                for (Hexagon h : map.getHexagons()) {
                    for (Hexagon h2 : p.getOwnedHexagons()) {
                        if (h.getLocation().equals(h2.getLocation())) {
                            h.setOwner(p);
                        }
                    }
                    h.setColorTexture();
                }

                for (Building b : p.getBuildings()) {
                    b.setImage();
                }
                for (Unit u : p.getUnits()) {
                    u.setTexture();
                }
            }
        });
    }

    public LocalGame(String ipAddress, String username) {
        getRemoteGame(ipAddress, username);
        fog = new Fog(thisPlayer, thisPlayer.getOwnedHexagons(), this.map);
    }

    private void join(String username) {
        try {
            this.thisPlayer = remoteGame.addPlayer(username);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void leaveGame() {
        try {
            remoteGame.leaveGame(thisPlayer.getId());
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public boolean isMyTurn() {
        try {
            return thisPlayer.getId() == remoteGame.getCurrentPlayer().getId();
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return false;
        }
    }

    public void endTurn() {
        try {
            remoteGame.endTurn(thisPlayer.getId());
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public Player getCurrentPlayer(){
        try {
            return remoteGame.getCurrentPlayer();
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
        return null;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Map getMap() {
        return map;
    }

    public Fog getFog() { return this.fog; }

    public void claimLand(Unit unit) {
        try {
            remoteGame.claimLand(unit);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public Unit getUnitPreset(UnitType type) {
        try {
            return remoteGame.getUnitPreset(type);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return null;
        }
    }

    public void buyUnit(UnitType type, Point location) {
        try {
            Hexagon unitLocation = remoteGame.buyUnit(type, location, thisPlayer.getId());
            if (unitLocation != null){
                getFog().unitCreated(unitLocation);
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void moveUnit(Unit u, Point location) {
        try {
            if (remoteGame.moveUnit(u, location, thisPlayer.getId(), walkableHexes(u.getLocation(), u.getMovementLeft()), pathDistance(u, location))){
                fog.unitMovement(map.getHexAtLocation(u.getLocation()), map.getHexAtLocation(location));
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public Unit getUnitOnHex(Hexagon hex) {
        Unit unit = null;
        for (Player p : getPlayers()) {
            for (Unit u : p.getUnits()) {
                if (u.getLocation().getX() == hex.getLocation().getX() && u.getLocation().getY() == hex.getLocation().getY()) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public Unit getSelectedUnit() {
        Unit unit = null;
        for (Unit u : thisPlayer.getUnits()) {
            if (u.getSelected()) {
                unit = u;
            }
        }
        return unit;
    }

    public int attackUnit(Unit attacker, Unit defender) {
        try {
            return remoteGame.attackUnit(attacker, defender);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return 0;
        }
    }

    public void sellUnit(Unit u) {
        try {
            remoteGame.sellUnit(u);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void attackBuilding(Unit attacker, Building b) {
        try {
            remoteGame.attackBuilding(attacker, b);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public Building getBuildingPreset(BuildingType type) {
        try {
            return remoteGame.getBuildingPreset(type);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return null;
        }
    }

    public void buyBuilding(BuildingType type, Point location) {
        try {
            remoteGame.buyBuilding(type, location);
            if (getBuildingAtLocation(location) != null && type == BuildingType.RESOURCE){
                map.getHexAtLocation(location).removeObjectType();
                map.getHexAtLocation(location).removeObject();
                getBuildingAtLocation(location).setMineOnMountain();
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public void sellBuilding(Point location) {
        try {
            remoteGame.sellBuilding(location);
            if (map.getHexAtLocation(location).getIsMountain()) {
                map.getHexAtLocation(location).setMountain();
            }
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public Building getBuildingAtLocation(Point location) {
        try {
            return remoteGame.getBuildingAtLocation(location);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return null;
        }
    }

    public Unit getUnitAtLocation(Point location){
        try {
            return remoteGame.getUnitAtLocation(location);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return null;
        }
    }

    public void setWalkableHexesForUnit(Unit unit) {
        unit.setWalkableHexes(walkableHexes(unit.getLocation(), unit.getMovementLeft()));
    }

    public List<Hexagon> walkableHexes(Point location, int radius){
        List<Hexagon> area = new ArrayList<>();
        List<Hexagon> frontier = new ArrayList<>();
        HashMap pathMap = new HashMap();
        Hexagon current;
        int i = 0;
        frontier.add(map.getHexAtLocation(location));
        while (true) {
            try {
                current = frontier.get(i);
            } catch (IndexOutOfBoundsException e) {
                LOGGER.severe("Pathfinding wrong");
                return area;
            }

            if (map.pathDistance(pathMap, current, map.getHexAtLocation(location)) == radius){
                lastPathGenerated = pathMap;
                return area;
            }
            for (Hexagon h : map.getHexesInRadius(current.getLocation(), 1)){
                if (!pathMap.containsKey(h) &&
                        h.getObjectType() != ObjectType.MOUNTAIN &&
                        h.getGroundType() != GroundType.WATER &&
                        getBuildingAtLocation(h.getLocation()) == null &&
                        getUnitAtLocation(h.getLocation()) == null){
                    frontier.add(h);
                    pathMap.put(h, current);
                    area.add(h);
                }
            }
            i++;
        }
    }

    public int pathDistance(Unit unit, Point location){
        Hexagon targetHex = map.getHexAtLocation(location);
        if (unit.getMovementLeft() == 0 ||
                targetHex.getObjectType() == ObjectType.MOUNTAIN ||
                targetHex.getGroundType() == GroundType.WATER ||
                getBuildingAtLocation(location) != null ||
                getUnitAtLocation(location) != null){
            return 1;
        }
        Hexagon current = map.getHexAtLocation(location);
        int i = 0;
        while (current.getLocation().x != unit.getLocation().x || current.getLocation().y != unit.getLocation().y){
            if (lastPathGenerated.containsKey(current)) {
                current = (Hexagon)lastPathGenerated.get(current);
                i++;
            } else {
                return 99;
            }
        }
        return i;
    }

    public void destroyBuilding(Building building){
        try {
            remoteGame.destroyBuilding(building);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public boolean hexEmpty(Point location) {
        try {
            return remoteGame.hexEmpty(location);
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL, e.getMessage());
            return false;
        }
    }

    private void getRemoteGame(String ipAddress, String username) {
        int portNumber = 1099;

        // Print IP address and port number for registry
        LOGGER.info("Client: IP Address: " + ipAddress);
        LOGGER.info("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            LOGGER.info("Client: Cannot locate registry");
            LOGGER.info("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            LOGGER.info("Client: Registry located");
        } else {
            LOGGER.info("Client: Cannot locate registry");
            LOGGER.info("Client: Registry is null pointer");
        }

        //Bind using registry
        IRemotePublisherForListener remotePublisher = null;
        if (registry != null) {
            try {
                remoteGame = (IRemoteGame) registry.lookup("ProjectHex");
                remotePublisher = (IRemotePublisherForListener) registry.lookup("Publisher");
            } catch (RemoteException e) {
                LOGGER.info("Client: RemoteException on IRemoteGame");
                LOGGER.info("Client: RemoteException: " + e.getMessage());
                remoteGame = null;
            } catch (NotBoundException e) {
                LOGGER.info("Client: Cannot bind IRemoteGame");
                LOGGER.info("Client: NotBoundException: " + e.getMessage());
                remoteGame = null;
            }
        }

        //Try to subscribe
        if (remotePublisher != null) {
            try {
                fontysListener = new FontysListener(this);
                remotePublisher.subscribeRemoteListener(fontysListener, "Players");
            } catch (RemoteException e) {
                LOGGER.severe("Client: subscribing to players went wrong");
                LOGGER.severe("Client: RemoteException: " + e.getMessage());
            }
        }

        // Test RMI connection
        if (remoteGame != null) {
            try {
                join(username);
                players = remoteGame.getPlayers();
                map = remoteGame.getMap();
                map.setTextures();
            } catch (RemoteException e) {
                LOGGER.log(Level.ALL, e.getMessage());
            }
            LOGGER.info("Client: remoteGame retrieved");
        } else {
            LOGGER.info("Client: Something went wrong");
            System.exit(0);
        }
    }

    public boolean lastPlayer() {
        try {
            return remoteGame.lastPlayer() && remoteGame.getVersion() != 1;
        } catch (RemoteException e) {
            LOGGER.log(Level.ALL,e.getMessage());
        }
        return false;
    }
}
