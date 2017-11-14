package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.shared.IGame;
import java.rmi.RemoteException;

public class PlayerTable extends Table {
    private Table t;
    private Label lGold;
    private Label lGpt;

    final private IGame game;
    private Skin skin;

    public PlayerTable(IGame game, Skin skin) throws RemoteException {
        this.game = game;
        this.skin = skin;

        t = new Table();
        lGold = new Label("GOLD: " + game.getCurrentPlayer().getGold(), skin);
        t.add(lGold).width(90);

        lGpt = new Label("GPT: " + game.getCurrentPlayer().getGoldPerTurn(), skin);
        if (game.getCurrentPlayer().getGoldPerTurn() < 0) {
            lGpt.setColor(Color.RED);
        } else {
            lGpt.setColor(Color.WHITE);
        }
        t.add(lGpt).width(90);

        t.setPosition(100, Gdx.graphics.getHeight() - 10);

        addActor(t);
    }

    public void update() {
        try {
            lGold.setText("GOLD: " + game.getCurrentPlayer().getGold());
            lGpt.setText("GPT: " + game.getCurrentPlayer().getGoldPerTurn());
            if (game.getCurrentPlayer().getGoldPerTurn() < 0) {
                lGpt.setColor(Color.RED);
            } else {
                lGpt.setColor(Color.WHITE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
