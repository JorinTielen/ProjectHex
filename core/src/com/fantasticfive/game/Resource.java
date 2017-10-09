package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;

import java.io.File;

public class Resource extends Building {

    private int productionPerTurn;

    public Resource(BuildingType buildingType, int health, Point location,
                    Texture image, int purchaseCost, GroundType[] buildableOn,
                    int productionPerTurn, Player owner) {
        super(buildingType, health, location, image, purchaseCost, buildableOn, owner);
    }
}
