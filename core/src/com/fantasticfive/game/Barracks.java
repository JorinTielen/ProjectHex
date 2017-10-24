package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;

public class Barracks extends Building{
    private UnitType[] creatableUnits;
    private int purchaseCost;

    public Barracks(int health, Texture image, int purchaseCost, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    public void setCreatableUnits(UnitType[] creatableUnits) {
        this.creatableUnits = creatableUnits;
    }

    public UnitType[] getCreatableUnits(){
        return creatableUnits;
    }

    public int getPurchaseCost() { return purchaseCost; }
}
