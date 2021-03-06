package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.shared.Building;

import java.util.logging.Logger;

public class BuildingSellTable extends Table {
    private static final Logger LOGGER = Logger.getLogger(BuildingSellTable.class.getName());

    private Table t;
    private Label l;

    final private LocalGame game;
    private Skin skin;
    private Building building;

    public BuildingSellTable(LocalGame game, Skin skin) {
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
                LOGGER.info("Selling building");
                BuildingSellTable.this.game.sellBuilding(building.getLocation());
                setVisible(false);
            }
        });

        addActor(t);
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
