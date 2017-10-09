package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;

public class Barracks extends Building{
    private UnitType[] creatableUnits;
  
    protected int purchaseCost;
    public Barracks(int health, Point location, Texture image, int purchaseCost, GroundType[] buildableOn, UnitType[] creatableUnits, Player owner) {
        super(health, location, image, buildableOn, owner);
        this.creatableUnits = creatableUnits;
        this.purchaseCost = purchaseCost;
    }

    public Barracks(int health, Texture image, int purchaseCost, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    public void setCreatableUnits(UnitType[] creatableUnits){
        this.creatableUnits = creatableUnits;
    }
}
