import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface IPlayer extends Serializable {
    String getUsername();
    enums.Color getColor();
    void addGold(int gold);
    void purchaseBuilding(Building building);
    void endTurn();
    List<IUnit> getUnits();
    IBuilding getBuildingAtLocation(Point location);
    void sellBuilding(IBuilding building, int cost);
    int getGold();
    void purchaseUnit(enums.UnitType);
}
