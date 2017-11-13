package com.fantasticfive.shared;

import java.rmi.Remote;

public interface IGame extends Remote {
    IUnit getSelectedUnit();
}
