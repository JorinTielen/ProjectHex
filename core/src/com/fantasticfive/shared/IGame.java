package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGame extends Remote {
    Unit getSelectedUnit() throws RemoteException;
    void setMap() throws RemoteException;
    void addPlayer(String username) throws RemoteException;
    void startGame() throws RemoteException;
    List<Player> getPlayers() throws RemoteException;
    Unit getUnitOnHex(Hexagon hex) throws RemoteException;
    Building getBuildingAtLocation(Point location) throws RemoteException;
    Player getCurrentPlayer() throws RemoteException;
    void createBuilding(BuildingType buildingType, Point location) throws RemoteException;
    boolean hexEmpty(Point location) throws RemoteException;
    void attackBuilding(Unit selectedUnit, Point locationBuilding) throws RemoteException;
    Unit getUnitPreset(UnitType unitType) throws RemoteException;
    void createUnit(UnitType unitType, Point location) throws RemoteException;
    void endTurn() throws RemoteException;
    void leaveGame() throws RemoteException;
    void sellBuilding(Point location) throws RemoteException;
    Building getBuildingPreset(BuildingType buildingType) throws RemoteException;
}
