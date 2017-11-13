package com.fantasticfive.shared;

import java.rmi.Remote;
import java.util.List;

public interface IGame extends Remote {
    IUnit getSelectedUnit();
    void setMap(Map map);
    void addPlayer(String username);
    void startGame();
    List<IPlayer> getPlayers();
    IUnit getUnitOnHex(Hexagon hex);

}
