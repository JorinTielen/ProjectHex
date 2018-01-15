package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.screens.MainMenuScreen;
import com.fantasticfive.server.ServerManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainMenuTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    public MainMenuTable(MainMenuScreen menuScreen) {
        t = new Table();

        final TextButton btnCreateServer = new TextButton("Create Server", menuScreen.skin);

        final TextButton btnJoinServer = new TextButton("Join Server", menuScreen.skin);

        final TextButton btnOptions = new TextButton("Options", menuScreen.skin);

        final TextButton btnExitGame = new TextButton("Exit", menuScreen.skin);

        t.add(btnCreateServer).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        t.add(btnJoinServer).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        t.add(btnOptions).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        t.add(btnExitGame).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnCreateServer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuTable.this.setVisible(false);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                menuScreen.future = executor.submit(new ServerManager());
                menuScreen.serverStarted = true;
                InetAddress localhost = null;
                try {
                    localhost = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                menuScreen.createServerTable.setIpAddress(localhost.getHostAddress());
                menuScreen.createServerTable.setVisible(true);
            }
        });

        btnJoinServer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuTable.this.setVisible(false);
                menuScreen.joinServerTable.setVisible(true);
            }
        });

        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuTable.this.setVisible(false);
                menuScreen.optionsTable.setVisible(true);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        addActor(t);
    }
}
