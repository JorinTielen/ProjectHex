package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

public class CreateServerTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private String ipAddress;
    private Label ipLabel;
    public TextButton btnCreateLobby;

    private int minPlayers = 2;
    private int maxPlayers = 6;

    public CreateServerTable(MainMenuScreen menuScreen) {
        t = new Table();

        final TextButton btnCopyToClipboard = new TextButton("Copy to clipboard", menuScreen.skin);

        final SelectBox selectNumberOfPlayers = new SelectBox(menuScreen.skin);
        String[] playersAmount = new String[maxPlayers - minPlayers + 1];
        for (int i = 0; i < (maxPlayers - minPlayers) + 1; i++) {
            playersAmount[i] = String.valueOf(i + minPlayers);
        }
        selectNumberOfPlayers.setItems(playersAmount);

        btnCreateLobby = new TextButton("Create lobby", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to menu", menuScreen.skin);

        t.add(ipLabel = new Label("Server ip address: ", menuScreen.skin));
        t.add(btnCopyToClipboard).width(collumnWidth / 2).height(collumnHeight / 2).pad(5);
        t.row();
        t.add(new Label("Max players: ", menuScreen.skin));
        t.add(selectNumberOfPlayers).pad(5);
        t.row();
        t.add(btnCreateLobby).fill().height(collumnHeight).colspan(2).pad(5);
        t.row();
        t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).colspan(2).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnCreateLobby.setText("Loading");
        btnCreateLobby.setTouchable(Touchable.disabled);

        btnCopyToClipboard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
                StringSelection stringSelection = new StringSelection(ipAddress);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        btnCreateLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              //  menuScreen.connectToServer(ipAddress, menuScreen.username);

                menuScreen.lobbyTable = new LobbyTable(menuScreen, minPlayers, Integer.valueOf((String) selectNumberOfPlayers.getSelected()));
                menuScreen.table.addActor(menuScreen.lobbyTable);
                menuScreen.lobbyTable.setVisible(true);
                CreateServerTable.this.setVisible(false);
                //TODO: Code to join lobby
            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnCreateLobby.setText("Loading");
                btnCreateLobby.setTouchable(Touchable.disabled);
                menuScreen.serverStarted = false;
                CreateServerTable.this.setVisible(false);
            }
        });


        addActor(t);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        ipLabel.setText("Server ip address: " + ipAddress);
    }
}
