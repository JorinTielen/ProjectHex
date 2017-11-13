package com.fantasticfive.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends Remote {
    IUnit getSelectedUnit()throws RemoteException;
    void setMap(Map map)throws RemoteException;
    void addPlayer(String username)throws RemoteException;
    void startGame()throws RemoteException;
    List<IPlayer> getPlayers()throws RemoteException;
    IUnit getUnitOnHex(Hexagon hex)throws RemoteException;

}
