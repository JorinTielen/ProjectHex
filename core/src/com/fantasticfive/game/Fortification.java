package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;

public class Fortification extends Building {
    protected int purchaseCost;

    public Fortification(int health, Point location, Texture image, int purchaseCost, GroundType[] buildableOn, Player owner) {
        super(health, location, image, buildableOn, owner);
        this.purchaseCost = purchaseCost;
    }

    public Fortification(int health, Texture image, int purchaseCost, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
    }
}
