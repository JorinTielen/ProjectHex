package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public interface IGame extends Remote {
    IUnit getSelectedUnit()throws RemoteException;
    void setMap(IMap map)throws RemoteException;
    void addPlayer(String username)throws RemoteException;
    void startGame()throws RemoteException;
    List<IPlayer> getPlayers()throws RemoteException;
    IUnit getUnitOnHex(Hexagon hex)throws RemoteException;
    IBuilding getBuildingAtLocation(Point location);
    IPlayer getCurrentPlayer();
    void createBuilding(BuildingType buildingType, Point location);
    boolean hexEmpty(Point location);
    void attackBuilding(IUnit selectedUnit, Point locationBuilding);
    IUnit getUnitPreset(UnitType unitType);
    void createUnit(UnitType unitType, Point location);
    void endTurn();
    void leaveGame();
    void sellBuilding(Point location);
    IBuilding getBuildingPreset(BuildingType buildingType);
}
