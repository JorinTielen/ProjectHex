package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.Database;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LobbyTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private Database database = new Database();
    private MainMenuScreen menuScreen;

    private List<Label> users = new ArrayList<>();

    public LobbyTable(MainMenuScreen menuScreen) {
        t = new Table();
        this.menuScreen = menuScreen;

        final TextButton btnReady = new TextButton("Ready?", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to main menu", menuScreen.skin);

        t.add(btnBack).pad(5);
        t.row();
        t.add(btnReady).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        t.add(new Label("Players", menuScreen.skin)).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnReady.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.ready(menuScreen.username);
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        Gdx.app.postRunnable(() -> {
                            if (menuScreen.getStarted()) {
                                menuScreen.join();
                            }
                        });
                    }
                }, 0, 250);
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
        playerReady("Jorin", true);
    }

    public void playerJoin(String username) {
        Label labelUser = new Label(username, menuScreen.skin);
        labelUser.setColor(Color.RED);

        t.row();
        t.add(labelUser).pad(5);
        t.row();

        users.add(labelUser);
    }

    public void playerReady(String username, Boolean ready) {
        for (Label l : users) {
            if (l.getText().toString().equals(username)) {
                if (ready) l.setColor(Color.GREEN);
                else l.setColor(Color.RED);
            }
        }
    }

    public void update() {

    }
}
