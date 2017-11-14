package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.shared.IGame;
import java.rmi.RemoteException;

public class OptionsTable extends Table {
    private Table t;

    final private IGame game;
    private Skin skin;

    public OptionsTable(IGame game, Skin skin) {
        setVisible(false);

        this.game = game;
        this.skin = skin;

        t = new Table();

        final TextButton buttonEndTurn = new TextButton("End turn", skin);
        t.add(buttonEndTurn).fill();
        t.row();

        final TextButton buttonLeaveGame = new TextButton("Leave game", skin);
        t.add(buttonLeaveGame).fill();
        t.row();

        t.setPosition(Gdx.graphics.getWidth() - 40, 55);

        buttonEndTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Ending turn");
                try {
                    OptionsTable.this.game.endTurn();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                setVisible(false);
            }
        });

        buttonLeaveGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Leaving game");
                try {
                    OptionsTable.this.game.leaveGame();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                setVisible(false);
            }
        });

        addActor(t);
    }
}
