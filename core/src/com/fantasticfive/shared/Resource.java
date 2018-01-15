package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.GroundType;

public class Resource extends Building {
    private int productionPerTurn;
    private int purchaseCost;

    public Resource(int health, int purchaseCost, int productionPerTurn, GroundType[] buildableOn) {
        super(health, buildableOn);
        this.purchaseCost = purchaseCost;
        this.productionPerTurn = productionPerTurn;
    }

    public int getPurchaseCost() {
        return purchaseCost;
    }

    public int getProductionPerTurn() {
        return this.productionPerTurn;
    }

    @Override
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public void setImage() {
        if (this.image != null) {
            image.dispose();
        }

        if (resourceOnMountain) {
            this.image = new Texture("mineMountain.png");
        } else {
            this.image = new Texture("mine.png");
        }
        if (owner != null) {
            setColor();
        }
    }
}
