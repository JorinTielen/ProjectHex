package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;

import java.util.logging.Logger;

public class OptionsTable extends Table {
    private static final Logger LOGGER = Logger.getLogger( OptionsTable.class.getName() );

    private Table t;
    private int colWidth = 130;
    private int colHeight = 40;

    final private LocalGame game;
    private Skin skin;

    public OptionsTable(LocalGame game, Skin skin) {
        this.game = game;
        this.skin = skin;

        t = new Table();

        final TextButton buttonEndTurn = new TextButton("End turn", skin);

       buttonEndTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("Ending turn");
                OptionsTable.this.game.endTurn();
            }
        });


        t.setPosition(Gdx.graphics.getWidth() - 150, 40);

        addActor(t);
    }
}
