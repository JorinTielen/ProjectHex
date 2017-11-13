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
    void purchaseUnit(UnitType unitType);
    List<IBuilding> getBuildings();

}
