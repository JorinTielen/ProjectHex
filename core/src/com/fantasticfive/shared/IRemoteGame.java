package com.fantasticfive.shared;

import com.fantasticfive.server.UnitFactory;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.UnitType;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteGame extends Remote {
    int getVersion() throws RemoteException;
    IPlayer addPlayer(String username) throws RemoteException;
    IUnit createUnit(UnitType unitType, Point location, int playerId) throws RemoteException;
    List<Player> getPlayers() throws RemoteException;
    Map getMap() throws RemoteException;
}
