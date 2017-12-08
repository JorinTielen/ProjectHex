package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
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

    public PlayerWinTable(Skin skin) {
        setVisible(false);

        t = new Table();
        l = new Label("", skin);

        t.add(l);

        addActor(t);
    }

    public void setEndGameLabel(LocalGame game) {
        Player winner = game.getCurrentPlayer();
        l.setText(winner.getUsername() + " has won the game!");

        if(winner.getUsername().equals(game.getThisPlayer().getUsername())) {
            l.setColor(Color.GREEN);
        } else {
            l.setColor(Color.RED);
        }
    }
}
