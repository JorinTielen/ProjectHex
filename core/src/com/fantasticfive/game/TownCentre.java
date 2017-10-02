package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;

import java.io.File;

public class TownCentre extends Building{
    public TownCentre(BuildingType buildingType, int health, Point location,
                      File image, int purchaseCost, GroundType[] buildableOn,
                      Player owner) {
        this.buildingType = buildingType;
        this.health = health;
        this.location = location;
        this.image = image;
        this.purchaseCost = purchaseCost;
        this.buildableOn = buildableOn;
        this.owner = owner;
    }
}
