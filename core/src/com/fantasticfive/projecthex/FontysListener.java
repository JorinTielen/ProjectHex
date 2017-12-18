package com.fantasticfive.projecthex;

import com.badlogic.gdx.Gdx;
import com.fantasticfive.shared.Player;
import fontyspublisher.IRemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.List;

public class FontysListener implements IRemotePropertyListener, Serializable {
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Gdx.app.postRunnable(() -> LocalGame.GetInstance().UpdateFromRemotePush((List<Player>) propertyChangeEvent.getNewValue()));
    }
}
