package com.fantasticfive.projecthex;

import com.badlogic.gdx.Gdx;
import com.fantasticfive.shared.IRemoteGame;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class LocalGame {
    private List<Player> players = new ArrayList<>();
    private Player thisPlayer;
    private Map map;

    private int version = 0;
    private IRemoteGame remoteGame;

    public LocalGame(String ipAddress, String username) {
        getRemoteGame(ipAddress, username);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    int remoteVersion = remoteGame.getVersion();
                    if (remoteVersion != version) {
                        Gdx.app.postRunnable(() ->
                            UpdateFromRemote(remoteVersion)
                        );
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },0, 250);
    }

    private void join(String username) {
        try {
            this.thisPlayer = remoteGame.addPlayer(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void leaveGame()  {
        try {
            remoteGame.leaveGame(thisPlayer.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void UpdateFromRemote(int remoteVersion) {
        try {
            players = remoteGame.getPlayers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        for (Player p : players) {
            if (this.thisPlayer.getId() == p.getId()) {
                this.thisPlayer = p;
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

        version = remoteVersion;
        System.out.println("Updated from Remote");
    }

    public void updateFromLocal() {
        try {
            remoteGame.updateFromLocal(players);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    boolean isMyTurn() {
        try {
            return thisPlayer.getId() == remoteGame.getCurrentPlayer().getId();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void endTurn() {
        try {
            remoteGame.endTurn(thisPlayer.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Map getMap() {
        return map;
    }

    public void claimLand(Unit unit){
        try {
            remoteGame.claimLand(unit);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Unit getUnitPreset(UnitType type) {
        try {
            return remoteGame.getUnitPreset(type);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void buyUnit(UnitType type, Point location) {
        try {
            remoteGame.buyUnit(type, location, thisPlayer.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void moveUnit(Unit u, Point location) {
        try {
            remoteGame.moveUnit(u, location, thisPlayer.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Unit getUnitOnHex(Hexagon hex) {
        Unit unit = null;
        for (Player p : getPlayers()) {
            for (Unit u : p.getUnits()) {
                if (u.getLocation().x == hex.getLocation().x && u.getLocation().y == hex.getLocation().y) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    //TODO: Make a singe unit selected?
    public Unit getSelectedUnit() {
        Unit unit = null;
        for (Player p : getPlayers()) {
            for (Unit u : p.getUnits()) {
                if (u.getSelected()) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    public void attackUnit(Unit attacker, Unit defender) {
        try {
            remoteGame.attackUnit(attacker, defender);
        } catch (RemoteException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public Building getBuildingPreset(BuildingType type) {
        try {
            return remoteGame.getBuildingPreset(type);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void buyBuilding(BuildingType type, Point location) {
        try {
            remoteGame.buyBuilding(type, location);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sellBuilding(Point location) {
        try {
            remoteGame.sellBuilding(location);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Building getBuildingAtLocation(Point location) {
        try {
            return remoteGame.getBuildingAtLocation(location);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setWalkableHexesForUnit(Unit unit){
        List<Hexagon> walkableHexes = new ArrayList<>();
        for (Hexagon hex : map.getHexagons()){
            if (map.canMoveTo(unit, hex.getLocation()) && hexEmpty(hex.getLocation())){
                walkableHexes.add(hex);
            }
        }
        unit.setWalkableHexes(walkableHexes);
    }

    public boolean hexEmpty(Point location) {
        try {
            return remoteGame.hexEmpty(location);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getRemoteGame(String ipAddress, String username) {
        int portNumber = 1099;

        // Print IP address and port number for registry
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }

        //Bind using registry
        if (registry != null) {
            try {
                remoteGame = (IRemoteGame) registry.lookup("ProjectHex");
            } catch (RemoteException e) {
                System.out.println("Client: RemoteException on IRemoteGame");
                System.out.println("Client: RemoteException: " + e.getMessage());
                remoteGame = null;
            } catch (NotBoundException e) {
                System.out.println("Client: Cannot bind IRemoteGame");
                System.out.println("Client: NotBoundException: " + e.getMessage());
                remoteGame = null;
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
                e.printStackTrace();
            }
            System.out.println("Client: remoteGame retrieved");
        } else {
            System.out.println("Client: Something went wrong");
            System.exit(0);
        }
    }
}
