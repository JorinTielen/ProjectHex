package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.shared.Building;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.enums.UnitType;

import java.util.logging.Logger;

public class UnitShopTable extends Table {
    private static final Logger LOGGER = Logger.getLogger( UnitShopTable.class.getName() );

    private Table t;

    final private LocalGame game;
    private Skin skin;
    private Building building;

    public UnitShopTable(LocalGame game, Skin skin) {
        setVisible(false);
        this.t = new Table();
        this.game = game;
        this.skin = skin;

        t.add(new Label("Buy Unit", skin)).fill();
        t.row();

        final TextButton buttonBuyArcher = new TextButton("Archer - Cost: " + game.getUnitPreset(UnitType.ARCHER).getPurchaseCost(), skin);
        t.add(buttonBuyArcher).fill();
        t.row();

        final TextButton buttonBuySwordsman = new TextButton("Swordsman - Cost: " + game.getUnitPreset(UnitType.SWORDSMAN).getPurchaseCost(), skin);
        t.add(buttonBuySwordsman).fill();
        t.row();

        final TextButton buttonBuyScout = new TextButton("Scout - Cost: " + game.getUnitPreset(UnitType.SCOUT).getPurchaseCost(), skin);
        t.add(buttonBuyScout).fill();
        t.row();

        final TextButton buttonSellBarracks = new TextButton("Sell Barracks", skin);
        t.add(buttonSellBarracks).fill();
        t.row();

        //Method for buying archer
        buttonBuyArcher.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("you bought an archer");
                UnitShopTable.this.game.buyUnit(UnitType.ARCHER, new Point(building.getLocation().x, building.getLocation().y));
                setVisible(false);
            }
        });

        //Method for buying swordsman
        buttonBuySwordsman.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("you bought a swordsman");

                UnitShopTable.this.game.buyUnit(UnitType.SWORDSMAN, new Point(building.getLocation().x, building.getLocation().y));
                setVisible(false);
            }
        });

        //Method for buying scout
        buttonBuyScout.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("you bought a scout");
                UnitShopTable.this.game.buyUnit(UnitType.SCOUT, new Point(building.getLocation().x, building.getLocation().y));
                setVisible(false);
            }
        });

        //Method for selling barracks
        buttonSellBarracks.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("You sold your Barracks.");
                UnitShopTable.this.game.sellBuilding(building.getLocation());
                setVisible(false);
            }
        });

        addActor(t);
    }

    public void setBuilding(Building b) {
        this.building = b;
    }
}
