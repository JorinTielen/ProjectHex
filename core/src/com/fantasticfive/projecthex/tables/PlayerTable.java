package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.projecthex.LocalGame;

import java.rmi.RemoteException;

public class PlayerTable extends Table {
    private Table t;
    private Label lGold;
    private Label lGpt;
    private Label lCUser;

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
        t.add(lGpt).width(70);

        if (game.isMyTurn()) {
            lCUser = new Label(" It is your turn!", skin);
            lCUser.setColor(Color.GREEN);
        } else {
            lCUser = new Label(" Please wait...", skin);
            lCUser.setColor(Color.RED);
        }
        t.add(lCUser).width(100).pad(5);

        t.setPosition(160, Gdx.graphics.getHeight() - 30);

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

        if (game.isMyTurn()) {
            lCUser.setText(" It is your turn!");
            lCUser.setColor(Color.GREEN);
        } else {
            lCUser.setText(" Please wait...");
            lCUser.setColor(Color.RED);
        }
    }
}
