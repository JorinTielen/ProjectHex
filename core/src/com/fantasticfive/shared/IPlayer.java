package com.fantasticfive.shared;

import java.io.Serializable;
import java.util.List;
import com.fantasticfive.shared.enums.*;

public interface IPlayer extends Serializable {
    String getUsername();
    Color getColor();
    void addGold(int gold);
    void purchaseBuilding(IBuilding building);
    void endTurn();
    List<IUnit> getUnits();
    IBuilding getBuildingAtLocation(Point location);
    void sellBuilding(IBuilding building, int cost);
    int getGold();
    void purchaseUnit(IUnit unit);
    List<IBuilding> getBuildings();
    void removeBuilding(IBuilding building);
    void removeUnit(IUnit unit);
    int getGoldPerTurn();
    void sellUnit(IUnit unit);
}
