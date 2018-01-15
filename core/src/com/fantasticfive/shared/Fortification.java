package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.GroundType;

public class Fortification extends Building {
    private int purchaseCost;

    public Fortification(int health, int purchaseCost, GroundType[] buildableOn) {
        super(health, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    public int getPurchaseCost() {
        return purchaseCost;
    }

    @Override
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public void setImage() {
        this.image = new Texture("tower.png");
        if (owner != null) {
            setColor();
        }
    }
}
