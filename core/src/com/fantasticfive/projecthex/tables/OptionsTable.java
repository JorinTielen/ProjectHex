package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;

public class OptionsTable extends Table {
    private Table t;

    final private LocalGame game;
    private Skin skin;

    public OptionsTable(LocalGame game, Skin skin) {
        this.game = game;
        this.skin = skin;

        t = new Table();

        final TextButton buttonEndTurn = new TextButton("End turn", skin);
        t.add(buttonEndTurn).fill();
        t.row();

        final TextButton buttonLeaveGame = new TextButton("Leave game", skin);
        t.add(buttonLeaveGame).fill();
        t.row();

        buttonEndTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Ending turn");
                OptionsTable.this.game.endTurn();
            }
        });

        buttonLeaveGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Leaving game");
                OptionsTable.this.game.leaveGame();
            }
        });

        t.setPosition(Gdx.graphics.getWidth() - 60, 40);

        addActor(t);
    }
}
