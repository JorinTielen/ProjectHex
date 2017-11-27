package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.shared.Building;
import com.fantasticfive.shared.IGame;
import java.rmi.RemoteException;

public class BuildingSellTable extends Table {
    private Table t;
    private Label l;

    final private IGame game;
    private Skin skin;
    private Building building;

    public BuildingSellTable(IGame game, Skin skin) {
        setVisible(false);

        this.game = game;
        this.skin = skin;
        t = new Table();

        t.add(new Label("Sell Building", skin)).fill();
        t.row();

        final TextButton buttonSellBuilding = new TextButton("Sell", skin);
        t.add(buttonSellBuilding).fill();
        t.row();

        //Method for selling a building
        buttonSellBuilding.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Selling building");
                try {
                    BuildingSellTable.this.game.sellBuilding(building.getLocation());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                setVisible(false);
            }
        });

        addActor(t);
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
