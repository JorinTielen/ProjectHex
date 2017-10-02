package com.fantasticfive.game;

import java.io.File;

public abstract class Building {

    protected BuildingType buildingType;
    protected int health;
    protected Point location;
    protected File image;
    protected int purchaseCost;
    protected GroundType[] buildableOn;
    protected Player owner;

    public void damageHealth(int hp) {
        throw new UnsupportedOperationException();
    }
}
