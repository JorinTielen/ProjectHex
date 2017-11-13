package com.fantasticfive.shared;

import java.io.Serializable;

public interface IUnit extends Serializable {
    Boolean getSelected();
    Point getLocation();
    void resetMoves();
    void toggleSelected();
    int getCostPerTurn();
}
