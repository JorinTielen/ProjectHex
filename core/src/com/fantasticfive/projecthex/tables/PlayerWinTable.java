package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.shared.Player;

import java.rmi.RemoteException;

public class PlayerWinTable extends Table {

    private Table t;
    private Label l;

    final private LocalGame game;
    private Skin skin;

    public PlayerWinTable(LocalGame game, Skin skin) throws RemoteException {
        setVisible(false);

        this.game = game;
        this.skin = skin;

        t = new Table();
        Player winner = game.getPlayers().get(0);
        l = new Label(winner.getUsername() + " has won the game!", skin);
        if(winner.getUsername().equals(game.getThisPlayer().getUsername())) {
            l.setColor(Color.GREEN);
        } else {
            l.setColor(Color.RED);
        }
        t.add(l);
        t.row();

        addActor(t);
    }

}
