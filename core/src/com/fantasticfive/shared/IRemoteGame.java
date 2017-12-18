package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteGame extends Remote {
    //RMI

    /**
     * Gets the version number.
     * @return the current version number of the game, increases when players do stuff.
     * @throws RemoteException when RMI breaks.
     */
    int getVersion() throws RemoteException;

    //GAME

    /**
     * Starts the game.
     * @throws RemoteException when RMI breaks.
     */
    void startGame() throws RemoteException;
    /**
     * Ends the turn of the given playerId, and starts someone else's turn.
     * @throws RemoteException when RMI breaks.
     */
    void endTurn(int playerId) throws RemoteException;
    /**
     * Makes the given playerId leave the game. This will end their turn.
     * @throws RemoteException when RMI breaks.
     */
    void leaveGame(int playerId) throws RemoteException;
    /**
     * Updates the remote game from a local game,
     * for when a client made changes to the list of players.
     * @throws RemoteException when RMI breaks.
     */
    void updateFromLocal(List<Player> players) throws RemoteException;
    /**
     * Adds a player to the game.
     * @throws RemoteException when RMI breaks.
     */
    Player addPlayer(String username) throws RemoteException;
    /**
     * Gets the list of players, this will be quite a lot of data.
     * @throws RemoteException when RMI breaks.
     */
    List<Player> getPlayers() throws RemoteException;
    /**
     * Gets the player who's turn it currently is.
     * @throws RemoteException when RMI breaks.
     */
    Player getCurrentPlayer() throws RemoteException;

    /**
     * Check if there is only one player left
     * @throws RemoteException when RMI breaks.
     */
    boolean lastPlayer() throws RemoteException;

    //UNITS

    /**
     * Gets the unit preset for a certain UnitType.
     * @throws RemoteException when RMI breaks.
     */
    Unit getUnitPreset(UnitType type) throws RemoteException;

    Unit getUnitAtLocation(Point location) throws RemoteException;
    /**
     * Makes the playerId buy a unit of unitType, at the location.
     * @param unitType the unitType the unit will be.
     * @param location the location the unit will be spawned
     * @param playerId your player id.
     * @throws RemoteException
     */
    void buyUnit(UnitType unitType, Point location, int playerId) throws RemoteException;

    /**
     * Makes a unit move to another location.
     * @param u the unit you want to move.
     * @param location the location you want to move to.
     * @param playerId your player id.
     * @throws RemoteException
     */
    boolean moveUnit(Unit u, Point location, int playerId, List<Hexagon> walkableHexes, int distance) throws RemoteException;

    /**
     * Makes a unit attack another.
     * @param attacker the unit attacking
     * @param defender the unit defending
     * @throws RemoteException
     */
    int attackUnit(Unit attacker, Unit defender) throws RemoteException;
    /**
     * Makes a unit attack another.
     * @param u the unit you want to sell.
     * @throws RemoteException
     */
    void sellUnit(Unit u) throws RemoteException;
    /**
     * Claims land for the current player.
     * @throws RemoteException when RMI breaks.
     */
    void claimLand(Unit unit) throws RemoteException;

    //BUILDINGS

    /**
     * Gets a building preset of buildingType.
     * @throws RemoteException when RMI breaks.
     */
    Building getBuildingPreset(BuildingType type) throws RemoteException;
    /**
     * Buys a building of buildingType, for the current player at location.
     * @throws RemoteException when RMI breaks.
     */
    void buyBuilding(BuildingType buildingType, Point location) throws RemoteException;
    /**
     * Sells the building at location if it is owned by the current player.
     * @throws RemoteException when RMI breaks.
     */
    void sellBuilding(Point location) throws RemoteException;
    /**
     * Makes unit attack the building at location.
     * @throws RemoteException when RMI breaks.
     */
    void attackBuilding(Unit unit, Building b) throws RemoteException;
    /**
     * Gets the building at location.
     * @throws RemoteException when RMI breaks.
     */
    Building getBuildingAtLocation(Point location) throws RemoteException;

    /**
     *
     * @param building
     * Removes the building at the location because it is destroyed.
     * @throws RemoteException
     */
    void destroyBuilding(Building building) throws RemoteException;

    //MAP

    /**
     * Gets the map. Used at the start of the game.
     * @throws RemoteException when RMI breaks.
     */
    Map getMap() throws RemoteException;
    /**
     * Checks if a unit is allowed to move on the hex at location.
     * @throws RemoteException when RMI breaks.
     */
    boolean hexEmpty(Point location) throws RemoteException;
}
