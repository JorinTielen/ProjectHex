package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

public class JoinServerTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    public JoinServerTable(MainMenuScreen menuScreen) {
        t = new Table();

        final TextField txtIP = new TextField("", menuScreen.skin);

        final TextButton btnConnect = new TextButton("Connect to server", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to menu", menuScreen.skin);

        t.add(new Label("Server IP: ", menuScreen.skin));
        t.add(txtIP).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        t.add(btnConnect).fill().height(collumnHeight).colspan(2).pad(5);
        t.row();
        t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).colspan(2).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnConnect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                 menuScreen.connectToServer(txtIP.getText(), menuScreen.username);
            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.joinServerTable.setVisible(false);
                menuScreen.mainMenuTable.setVisible(true);
            }
        });

        addActor(t);
    }
}
