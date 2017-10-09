package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;

import java.io.File;

public class Barracks extends Building{

    private UnitType[] creatableUnits;

    public Barracks(BuildingType buildingType, int health, Point location,
                    Texture image, int purchaseCost, GroundType[] buildableOn,
                    UnitType[] creatableUnits, Player owner) {
        super(buildingType, health, location, image, purchaseCost, buildableOn, owner);
    }
}
