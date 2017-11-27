package com.fantasticfive.shared;

import com.fantasticfive.server.UnitFactory;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteGame extends Remote {
    //RMI
    int getVersion() throws RemoteException;
    //GAME
    void startGame() throws RemoteException;
    void endTurn(int playerId) throws RemoteException;
    void leaveGame(int playerId) throws RemoteException;
    Player addPlayer(String username) throws RemoteException;
    List<Player> getPlayers() throws RemoteException;
    Player getCurrentPlayer() throws RemoteException;
    //UNITS
    void attackBuilding(Unit unit, Point location) throws RemoteException;
    Unit getUnitPreset(UnitType type) throws RemoteException;
    void buyUnit(UnitType unitType, Point location, int playerId) throws RemoteException;
    //BUILDINGS
    Building getBuildingPreset(BuildingType type) throws RemoteException;
    void buyBuilding(BuildingType buildingType, Point location) throws RemoteException;
    void sellBuilding(Point location) throws RemoteException;
    Building getBuildingAtLocation(Point location) throws RemoteException;
    //MAP
    Map getMap() throws RemoteException;
    boolean hexEmpty(Point location) throws RemoteException;
}
