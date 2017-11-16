package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends Remote {
    IUnit getSelectedUnit() throws RemoteException;
    void setMap(IMap map) throws RemoteException;
    void addPlayer(String username) throws RemoteException;
    void startGame() throws RemoteException;
    List<IPlayer> getPlayers() throws RemoteException;
    IUnit getUnitOnHex(Hexagon hex) throws RemoteException;
    IBuilding getBuildingAtLocation(Point location) throws RemoteException;
    IPlayer getCurrentPlayer() throws RemoteException;
    void createBuilding(BuildingType buildingType, Point location) throws RemoteException;
    boolean hexEmpty(Point location) throws RemoteException;
    void attackBuilding(IUnit selectedUnit, Point locationBuilding) throws RemoteException;
    IUnit getUnitPreset(UnitType unitType) throws RemoteException;
    void createUnit(UnitType unitType, Point location) throws RemoteException;
    void endTurn() throws RemoteException;
    void leaveGame() throws RemoteException;
    void sellBuilding(Point location) throws RemoteException;
    Building getBuildingPreset(BuildingType buildingType) throws RemoteException;
}
