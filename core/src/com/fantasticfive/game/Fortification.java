package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;

import java.io.File;

public class Fortification extends Building {

    public Fortification(BuildingType buildingType, int health, Point location,
                         File image, int purchaseCost, GroundType[] buildableOn,
                         Player owner) {
        super(buildingType, health, location, image, purchaseCost, buildableOn, owner);

    }
}
