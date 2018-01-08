package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CreateServerTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private String ipAddress;
    private Label ipLabel;
    public TextButton btnStartGame;

    public CreateServerTable(MainMenuScreen menuScreen) {
        t = new Table();

        final TextButton btnCopyToClipboard = new TextButton("Copy to clipboard", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to menu", menuScreen.skin);

        btnStartGame = new TextButton("Start Game", menuScreen.skin);

        t.add(ipLabel = new Label("Server ip address: ", menuScreen.skin));
        t.add(btnCopyToClipboard).width(collumnWidth / 2).height(collumnHeight / 2).pad(5);
        t.row();
        t.add(btnStartGame).fill().height(collumnHeight).colspan(2).pad(5);
        t.row();
        t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).colspan(2).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnStartGame.setText("Loading");
        btnStartGame.setTouchable(Touchable.disabled);

        btnCopyToClipboard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
                StringSelection stringSelection = new StringSelection(ipAddress);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        btnStartGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.connectToServer(ipAddress, menuScreen.username);
                CreateServerTable.this.setVisible(false);
                menuScreen.lobbyTable.setVisible(true);
            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnStartGame.setText("Loading");
                btnStartGame.setTouchable(Touchable.disabled);
                menuScreen.serverStarted = false;
                CreateServerTable.this.setVisible(false);
                menuScreen.mainMenuTable.setVisible(true);
            }
        });


        addActor(t);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        ipLabel.setText("Server ip address: " + ipAddress);
    }
}
