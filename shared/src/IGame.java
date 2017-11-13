import java.rmi.Remote;

public interface IGame extends Remote {
    IUnit getSelectedUnit();
}
