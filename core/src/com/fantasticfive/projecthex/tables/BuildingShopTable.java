package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.projecthex.screens.GameScreen;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;

public class BuildingShopTable extends Table {
    private Table t;

    final private GameScreen gameScreen;

    final private LocalGame game;
    private Skin skin;
    private Building buildingToBuild;

    public BuildingShopTable(GameScreen gameScreen, LocalGame game, Skin skin) {
        setVisible(false);
        t = new Table();
        this.gameScreen = gameScreen;

        this.game = game;
        this.skin = skin;

        Table t = new Table();
        t.add(new Label("Buy Building", skin)).fill();
        t.row();

        final TextButton buttonBuyResource = new TextButton("Resource - Cost: " + ((Resource) game.getBuildingPreset(BuildingType.RESOURCE)).getPurchaseCost(), skin);
        t.add(buttonBuyResource).fill();
        t.row();

        final TextButton buttonBuyFortification = new TextButton("Fortification - Cost: " + ((Fortification) game.getBuildingPreset(BuildingType.FORTIFICATION)).getPurchaseCost(), skin);
        t.add(buttonBuyFortification).fill();
        t.row();

        final TextButton buttonBuyBarracks = new TextButton("Barracks - Cost: " + ((Barracks) game.getBuildingPreset(BuildingType.BARRACKS)).getPurchaseCost(), skin);
        t.add(buttonBuyBarracks).fill();
        t.row();

        //Method for buying Resource
        buttonBuyResource.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("You bought a Resource");
                buildingToBuild = BuildingShopTable.this.game.getBuildingPreset(BuildingType.RESOURCE);
                update();
                setVisible(false);
            }
        });

        //Method for buying Fortification
        buttonBuyFortification.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("You bought a Fortification");
                buildingToBuild = BuildingShopTable.this.game.getBuildingPreset(BuildingType.FORTIFICATION);
                update();
                setVisible(false);
            }
        });

        //Method for buying Barracks
        buttonBuyBarracks.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("You bought a Barracks");
                buildingToBuild = BuildingShopTable.this.game.getBuildingPreset(BuildingType.BARRACKS);
                update();
                setVisible(false);
            }
        });

        addActor(t);
    }

    private void update() {
        gameScreen.updateBuildingToBuild(buildingToBuild);
    }
}
