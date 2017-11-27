package com.fantasticfive.projecthex;

import com.badlogic.gdx.Gdx;
import com.fantasticfive.shared.IRemoteGame;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class LocalGame {
    private List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private int thisPlayerId;
    private Map map;

    private int version = 0;
    private IRemoteGame remoteGame;

    LocalGame() {
        getRemoteGame();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    int remoteVersion = remoteGame.getVersion();
                    if (remoteVersion != version) {
                        Gdx.app.postRunnable(() -> {
                            try {
                                UpdateFromRemote(remoteVersion);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },0, 500);
    }

    private void join(String username) throws RemoteException {
        thisPlayerId = remoteGame.addPlayer(username).getId();
    }

    private void UpdateFromRemote(int remoteVersion) throws RemoteException {
        players = remoteGame.getPlayers();

        for (Player p : players) {
            for (Building b : p.getBuildings()) {
                b.setImage();
            }
        }

        version = remoteVersion;
        System.out.println("Updated from Remote");
    }

    List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    Map getMap() {
        return map;
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

    private void getRemoteGame() {
        //Get ip address of server
        Scanner input = new Scanner(System.in);
        System.out.print("Client: Enter IP address of server: ");
        String ipAddress = input.nextLine();

        //Get port number
        int portNumber = 1099;

        //get username
        System.out.print("Client: Enter username: ");
        String username = input.nextLine();

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
