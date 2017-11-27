package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.shared.IGame;
import java.rmi.RemoteException;

public class PlayerTable extends Table {
    private Table t;
    private Label lGold;
    private Label lGpt;

    final private LocalGame game;
    private Skin skin;

    public PlayerTable(LocalGame game, Skin skin) throws RemoteException {
        this.game = game;
        this.skin = skin;

        t = new Table();
        lGold = new Label("GOLD: " + game.getThisPlayer().getGold(), skin);
        t.add(lGold).width(90);

        lGpt = new Label("GPT: " + game.getThisPlayer().getGoldPerTurn(), skin);
        if (game.getThisPlayer().getGoldPerTurn() < 0) {
            lGpt.setColor(Color.RED);
        } else {
            lGpt.setColor(Color.WHITE);
        }
        t.add(lGpt).width(90);

        t.setPosition(100, Gdx.graphics.getHeight() - 10);

        addActor(t);
    }

    public void update() {
        lGold.setText("GOLD: " + game.getThisPlayer().getGold());
        lGpt.setText("GPT: " + game.getThisPlayer().getGoldPerTurn());
        if (game.getThisPlayer().getGoldPerTurn() < 0) {
            lGpt.setColor(Color.RED);
        } else {
            lGpt.setColor(Color.WHITE);
        }
    }
}
