package com.fantasticfive.projecthex;

import com.fantasticfive.shared.Player;
import fontyspublisher.IRemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class FontysListener extends UnicastRemoteObject implements IRemotePropertyListener {
    private LocalGame localGame;
    protected FontysListener(LocalGame localGame) throws RemoteException {
        this.localGame = localGame;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        localGame.UpdateFromRemotePush((List<Player>) propertyChangeEvent.getNewValue());
    }
}
