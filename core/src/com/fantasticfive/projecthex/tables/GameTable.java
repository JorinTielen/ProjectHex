package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.screens.GameScreen;

public class GameTable extends Table {
    private Table t;

    private Skin skin;

    public GameTable(final GameScreen gameScreen, Skin skin) {
        t = new Table();
        this.skin = skin;

        TextButton buttonOptions = new TextButton("Options", skin);
        t.add(buttonOptions).fill();

        buttonOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Open Options");
                //gameScreen.showOptionsUI();
            }
        });

        t.setPosition(Gdx.graphics.getWidth() - 60, 15);

        addActor(t);
    }
}
