package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.*;

public class TownCentre extends Building {
    public TownCentre(int health, GroundType[] buildableOn) {
        super(health, buildableOn);
    }

    @Override
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public void setImage() {
        this.image = new Texture("townCentre.png");
        if(owner != null) {
            setColor();
        }
    }
}
