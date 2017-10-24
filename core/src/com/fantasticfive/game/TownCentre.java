package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;

public class TownCentre extends Building {
    public TownCentre(int health, Point location, Texture image, GroundType[] buildableOn, Player owner) {
        super(health, location, image, buildableOn, owner);
    }
}
