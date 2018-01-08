package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.Database;
import com.fantasticfive.projecthex.screens.MainMenuScreen;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private Database database = new Database();
    private MainMenuScreen menuScreen;

    private boolean owner;
    private HashMap<Label, Label> users = new HashMap<>();
    private Label labelPlayers;

    public LobbyTable(MainMenuScreen menuScreen, int minPlayers, int maxPlayers) {
        t = new Table();
        this.menuScreen = menuScreen;
        if (maxPlayers != 0) {
            owner = true;
        }

        final TextButton btnReady = new TextButton("Ready?", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to main menu", menuScreen.skin);

        labelPlayers = new Label("0/6 players joined", menuScreen.skin);

        t.add(btnBack);
        t.add(labelPlayers).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        t.add(btnReady).width(collumnWidth).height(collumnHeight).pad(5).colspan(2);
        t.row();
        t.add(new Label("Username", menuScreen.skin)).width(collumnWidth).height(collumnHeight).pad(5);
        t.add(new Label("Ready?", menuScreen.skin)).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {



            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.mainMenuTable.setVisible(true);
                LobbyTable.this.setVisible(false);
            }
        });

        addActor(t);

        playerJoin(menuScreen.username);
        playerJoin("Sven");
        playerJoin("Jorin");
    }

    public void playerJoin(String username) {
        Label labelUser = new Label(username, menuScreen.skin);
        Label labelReady = new Label("no", menuScreen.skin);

        t.row();
        t.add(labelUser).width(collumnWidth).pad(5);
        t.add(labelReady).width(collumnWidth).pad(5);
        t.row();

        users.put(labelUser, labelReady);
    }

    public void update() {

    }
}
