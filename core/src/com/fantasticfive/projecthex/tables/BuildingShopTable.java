package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.projecthex.ProjectHex;
import java.rmi.RemoteException;

public class BuildingShopTable extends Table {
    private Table t;

    final private ProjectHex projectHex;

    final private IGame game;
    private Skin skin;
    private IBuilding buildingToBuild;

    public BuildingShopTable(ProjectHex projectHex, IGame game, Skin skin) throws RemoteException {
        setVisible(false);
        t = new Table();
        this.projectHex = projectHex;

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
                try {
                    buildingToBuild = BuildingShopTable.this.game.getBuildingPreset(BuildingType.RESOURCE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                update();
                setVisible(false);
            }
        });

        //Method for buying Fortification
        buttonBuyFortification.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("You bought a Fortification");
                try {
                    buildingToBuild = BuildingShopTable.this.game.getBuildingPreset(BuildingType.FORTIFICATION);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                update();
                setVisible(false);
            }
        });

        //Method for buying Barracks
        buttonBuyBarracks.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("You bought a Barracks");
                try {
                    buildingToBuild = BuildingShopTable.this.game.getBuildingPreset(BuildingType.BARRACKS);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                update();
                setVisible(false);
            }
        });

        addActor(t);
    }

    private void update() {
        //projectHex.updateBuildingToBuild(buildingToBuild);
    }
}
