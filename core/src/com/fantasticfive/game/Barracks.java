package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;

import java.io.File;

public class Barracks extends Building{

    private UnitType[] creatableUnits;

    public Barracks(BuildingType buildingType, int health, Point location,
                    File image, int purchaseCost, GroundType[] buildableOn,
                    UnitType[] creatableUnits, Player owner) {
        this.buildingType = buildingType;
        this.health = health;
        this.location = location;
        this.image = image;
        this.purchaseCost = purchaseCost;
        this.buildableOn = buildableOn;
        this.creatableUnits = creatableUnits;
        this.owner = owner;
    }
}
