package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.UnitType;

/**
 * The barracks class. Extends the com.fantasticfive.shared.Building class. Used in the game to purchase units.
 */
public class Barracks extends Building {
    private UnitType[] creatableUnits;
    private int purchaseCost;

    public Barracks(int health, int purchaseCost, GroundType[] buildableOn) {
        super(health, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    /**
     * Set the units barracks can produce
     *
     * @param creatableUnits List of units barracks can produce
     */
    public void setCreatableUnits(UnitType[] creatableUnits) {
        this.creatableUnits = creatableUnits;
    }

    /**
     * Get a list of units barracks can produce
     *
     * @return List of units barracks can produce
     */
    public UnitType[] getCreatableUnits() {
        return creatableUnits;
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
        this.image = new Texture("barracks.png");
        if (owner != null) {
            setColor();
        }
    }
}
