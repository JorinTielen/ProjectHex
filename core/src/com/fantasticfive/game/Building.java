package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;

public abstract class Building {
    public Texture image;

    protected int health;
    protected Point location;
    protected GroundType[] buildableOn;
    protected Player owner;

    public Building(int health, Point location, Texture image, GroundType[] buildableOn, Player owner){
        this.health = health;
        this.location = location;
        this.image = image;
        this.buildableOn = buildableOn;
        this.owner = owner;
    }

    public Building(int health, Texture image, GroundType[] buildableOn){
        this.health = health;
        this.image = image;
        this.buildableOn = buildableOn;
    }

    public void setLocation(Point location){
        this.location = location;
    }

    public void setOwner(Player owner){
        this.owner = owner;
    }

    public boolean damageHealth(int hp) {
        health = health - hp;
        if (health <= 0){
            return true;
        }
        return false;
    }

    public Point getLocation() {
        return location;
    }
}
