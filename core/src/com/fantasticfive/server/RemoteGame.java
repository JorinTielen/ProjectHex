package com.fantasticfive.server;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;
import fontyspublisher.*;

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

    ArrayList<Color> usedColors = new ArrayList<>();
    ArrayList<Integer> usedIds = new ArrayList<>();

    private BuildingFactory buildingFactory = new BuildingFactory();
    private UnitFactory unitFactory = new UnitFactory();

    private Registry registry;
    private RemotePublisher remotePublisher = new RemotePublisher();

    RemoteGame(int portNumber) throws RemoteException {
        remotePublisher.registerProperty("players");
        RemoteSetup(portNumber);

        map = new Map(10, 10);
    }

    @Override
    public int getVersion() throws RemoteException {
        return version;
    }

    //TODO: fix de locaties van town centers
    @Override
    public IPlayer addPlayer(String username) {
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
        if (username.equals("enemy")) {
            Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(10, 13), p);
            p.purchaseBuilding(b);
        } else {
            Building b = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1, 0), p);
            p.purchaseBuilding(b);
        }

        players.add(p);
        version++;
        return p;
    }

    @Override
    public IUnit createUnit(UnitType unitType, Point location, int playerId) {
        return null;
    }

    @Override
    public List<Player> getPlayers() throws RemoteException {
        return players;
    }

    @Override
    public Map getMap() throws RemoteException {
        return map;
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
            registry.rebind("ProjectHex", this);
            System.out.println("Server: Game bound to registry");
        } catch (RemoteException e) {
            System.out.println("Server: Cannot bind Game");
            System.out.println("Server: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Server: Port already in use. \nServer: Please check if the server isn't already running");
            System.out.println("Server: NullPointerException: " + e.getMessage());
        }
    }
}
