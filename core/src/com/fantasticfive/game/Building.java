package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;

import java.io.File;

public abstract class Building {

    protected BuildingType buildingType;
    protected int health;
    protected Point location;
    protected File image;
    protected int purchaseCost;
    protected GroundType[] buildableOn;
    protected Player owner;

    public Building(BuildingType buildingType, int health, Point location, File image, int purchaseCost, GroundType[] buildableOn, Player owner){
        this.buildingType = buildingType;
        this.health = health;
        this.location = location;
        this.image = image;
        this.purchaseCost = purchaseCost;
        this.buildableOn = buildableOn;
        this.owner = owner;
    }

    public void damageHealth(int hp) {
        throw new UnsupportedOperationException();
    }
}
