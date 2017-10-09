package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;

import java.io.File;

public class Resource extends Building {

    private int productionPerTurn;
    protected int purchaseCost;
    public Resource(int health, Point location,
                    Texture image, int purchaseCost, GroundType[] buildableOn,
                    int productionPerTurn, Player owner) {
        super(health, location, image, buildableOn, owner);
        this.purchaseCost = purchaseCost;
        this.productionPerTurn = productionPerTurn;
    }

    public Resource(int health, Texture image, int purchaseCost,
                    int productionPerTurn, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
        this.productionPerTurn = productionPerTurn;
    }

    public void setProductionPerTurn(int productionPerTurn){
        this.productionPerTurn = productionPerTurn;
    }
}
