import java.io.Serializable;
import java.rmi.RemoteException;

public interface IPlayer extends Serializable {
    String getUsername();
    Color getColor();
    void addGold(int gold);
    void purchaseBuilding(Building building);
    void endTurn();
}
