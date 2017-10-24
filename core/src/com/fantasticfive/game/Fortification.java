package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;

public class Fortification extends Building {
    private int purchaseCost;

    public Fortification(int health, Texture image, int purchaseCost, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    public int getPurchaseCost() { return purchaseCost; }
}
