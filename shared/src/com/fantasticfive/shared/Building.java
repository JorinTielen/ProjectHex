package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.*;

/**
 * The Buidling class contains all the base information and functionality that all buildings share.
 */
public abstract class Building implements Cloneable {
    public Texture image;
    protected int health;
    protected GroundType[] buildableOn;
    private Point location;
    private Player owner;

    public Building(int health, Point location, Texture image, GroundType[] buildableOn, Player owner) {
        this.image = image;
        this.health = health;
        this.buildableOn = buildableOn;
        this.location = location;
        this.owner = owner;
    }

    public Building(int health, Texture image, GroundType[] buildableOn) {
        this.health = health;
        this.image = image;
        this.buildableOn = buildableOn;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }

    /**
     * Damages the building. The amount of hp a building has can never go below 0
     * @param hp The amount of hp to reduce from the building
     * @return
     */
    public boolean damageHealth(int hp) {
        health -= hp;
        if(health <= 0){
            health = 0;
        }
        System.out.println("Health has been reduced with " + hp + " to " + health);
        return health <= 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
