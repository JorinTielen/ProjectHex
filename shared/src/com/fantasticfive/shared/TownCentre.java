package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.*;

public class TownCentre extends Building {
    public TownCentre(int health, Point location, Texture image, GroundType[] buildableOn, IPlayer owner) {
        super(health, location, image, buildableOn, owner);
    }

    @Override
    public void setOwner(IPlayer owner) {
        this.owner = owner;
    }
}
