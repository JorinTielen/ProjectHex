package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.projecthex.screens.*;

import java.util.logging.Logger;

public class GameMenuTable extends Table {
    private static final Logger LOGGER = Logger.getLogger(GameMenuTable.class.getName());

    private Table t;
    private float collumnWidth = Gdx.graphics.getWidth() / 100 * 25;
    private float collumnHeight = Gdx.graphics.getHeight() / 100 * 6;

    final private LocalGame localGame;
    private Skin skin;


    public GameMenuTable(GameMain game, LocalGame localGame, GameScreen gameScreen, Skin skin) {
        this.localGame = localGame;
        this.skin = skin;

        t = new Table();

        final TextButton btnResumeGame = new TextButton("Resume", skin);

        final TextButton btnQuitMainMenu = new TextButton("Quit to main menu", skin);

        final TextButton btnQuitDesktop = new TextButton("Quit to desktop", skin);

        t.add(btnResumeGame).width(collumnWidth).height(collumnHeight).pad(5);
        t.row();
        //   t.add(btnQuitMainMenu).width(collumnWidth).height(collumnHeight).pad(5); //TODO: when making a new mainMenuScreen, somehow the map wont show and creating a new server crashes the game
        t.row();
        t.add(btnQuitDesktop).width(collumnWidth).height(collumnHeight).pad(5);

        btnResumeGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMenuTable.this.setVisible(false);
                gameScreen.inMenu = false;
            }
        });

        btnQuitMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("Leaving game (to main menu)");
                GameMenuTable.this.localGame.leaveGame();
                game.setScreen(new MainMenuScreen(game));
                gameScreen.dispose();
            }
        });

        btnQuitDesktop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("Leaving game (to desktop)");
                GameMenuTable.this.localGame.leaveGame();
                Gdx.app.exit();
            }
        });

        addActor(t);
    }
}
