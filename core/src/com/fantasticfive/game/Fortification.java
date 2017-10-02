package com.fantasticfive.game;

import java.io.File;

public class Fortification extends Building {

    public Fortification(BuildingType buildingType, int health, Point location,
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
