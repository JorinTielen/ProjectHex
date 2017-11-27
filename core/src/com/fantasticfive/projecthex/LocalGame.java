package com.fantasticfive.projecthex;

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
    private Map map;

    private int version = 0;
    private IRemoteGame remoteGame;
    private Registry registry;
    private Timer timer;

    public LocalGame() {
        getRemoteGame();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    int remoteVersion = remoteGame.getVersion();
                    if (remoteVersion != version) {
                        players = remoteGame.getPlayers();
                        version = remoteVersion;
                        System.out.println("Updated from Remote");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        },0, 500);
    }

    public void join(String username) {
        try {
            Player p = (Player) remoteGame.addPlayer(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Map getMap() {
        return map;
    }

    public IUnit getUnitOnHex(Hexagon hex) {
        IUnit unit = null;
        for (Player p : getPlayers()) {
            for (IUnit u : p.getUnits()) {
                if (u.getLocation().x == hex.getLocation().x && u.getLocation().y == hex.getLocation().y) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    //TODO: Make a singe unit selected?
    public IUnit getSelectedUnit() {
        IUnit unit = null;
        for (IPlayer p : getPlayers()) {
            for (IUnit u : p.getUnits()) {
                if (u.getSelected()) {
                    unit = u;
                }
            }
        }
        return unit;
    }

    /*private class PlayersRemoteListener implements IRemotePropertyListener {
        PlayersRemoteListener() {
            try {
                publisherForListener.subscribeRemoteListener(playersRemoteListener, "players");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
            players = (ArrayList<Player>) propertyChangeEvent.getNewValue();
        }
    } */

    private void getRemoteGame() {
        //Get ip address of server
        Scanner input = new Scanner(System.in);
        System.out.print("Client: Enter IP address of server: ");
        String ipAddress = input.nextLine();

        //Get port number
        System.out.print("Client: Enter port number: ");
        int portNumber = input.nextInt();

        // Print IP address and port number for registry
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
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
