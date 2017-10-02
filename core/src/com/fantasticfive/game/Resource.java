package com.fantasticfive.game;

import java.io.File;

public class Resource extends Building {

    private int productionPerTurn;

    public Resource(BuildingType buildingType, int health, Point location,
                    File image, int purchaseCost, GroundType[] buildableOn,
                    int productionPerTurn, Player owner) {
        this.buildingType = buildingType;
        this.health = health;
        this.location = location;
        this.image = image;
        this.purchaseCost = purchaseCost;
        this.buildableOn = buildableOn;
        this.productionPerTurn = productionPerTurn;
        this.owner = owner;
    }
}
