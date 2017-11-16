package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.*;

/**
 * The Buidling class contains all the base information and functionality that all buildings share.
 */
public abstract class Building implements Cloneable, IBuilding {
    public Texture image;
    protected int health;
    protected GroundType[] buildableOn;
    private Point location;
    protected IPlayer owner;

    public Building(int health, GroundType[] buildableOn) {
        this.health = health;
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

    public IPlayer getOwner() {
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

    public void setImage(){

    }

    public Texture getImage() {
        return image;
    }
}
