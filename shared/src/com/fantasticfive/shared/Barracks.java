package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.*;
import com.badlogic.gdx.graphics.Texture;

/**
 * The barracks class. Extends the com.fantasticfive.shared.Building class. Used in the game to purchase units.
 */
public class Barracks extends Building{
    private UnitType[] creatableUnits;
    private int purchaseCost;

    public Barracks(int health, Texture image, int purchaseCost, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    /**
     * Set the units barracks can produce
     * @param creatableUnits List of units barracks can produce
     */
    public void setCreatableUnits(UnitType[] creatableUnits) {
        this.creatableUnits = creatableUnits;
    }

    /**
     * Get a list of units barracks can produce
     * @return List of units barracks can produce
     */
    public UnitType[] getCreatableUnits(){
        return creatableUnits;
    }

    public int getPurchaseCost() { return purchaseCost; }
}
