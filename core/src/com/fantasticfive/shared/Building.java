package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.GroundType;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * The Buidling class contains all the base information and functionality that all buildings share.
 */
public abstract class Building implements Cloneable, Serializable {
    private static final Logger LOGGER = Logger.getLogger(Building.class.getName());

    public transient Texture image; //NOSONAR
    protected int health;
    protected GroundType[] buildableOn;
    private Point location;
    protected Player owner;
    private int maxHealth;
    private boolean destroyed;
    protected boolean resourceOnMountain;

    public Building(int health, GroundType[] buildableOn) {
        this.health = health;
        this.buildableOn = buildableOn;
        this.maxHealth = health;
        this.destroyed = false;
        this.resourceOnMountain = false;
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

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public boolean getDestroyed() {
        return this.destroyed;
    }

    public void destroy() {
        this.destroyed = true;
    }

    public void setResourceOnMountain(boolean resourceOnMountain) {
        this.resourceOnMountain = resourceOnMountain;
    }

    public void setImage() {
        //Needs to be empty, is overwritten in inheritance classes
    }

    public Texture getImage() {
        return image;
    }

    /**
     * Damages the building. The amount of hp a building has can never go below 0
     *
     * @param hp The amount of hp to reduce from the building
     * @return
     */
    public boolean damageHealth(int hp) {
        health -= hp;
        if (health <= 0) {
            health = 0;
        }
        LOGGER.info("Health has been reduced with " + hp + " to " + health);
        return health <= 0;
    }

    public void setColor() {
        image.getTextureData().prepare();
        Pixmap pixmap = image.getTextureData().consumePixmap();
        com.badlogic.gdx.graphics.Color newColor;
        switch (owner.getColor()) {
            case RED:
                newColor = com.badlogic.gdx.graphics.Color.RED;
                break;
            case BLUE:
                newColor = com.badlogic.gdx.graphics.Color.BLUE;
                break;
            case PURPLE:
                newColor = com.badlogic.gdx.graphics.Color.PURPLE;
                break;
            case ORANGE:
                newColor = com.badlogic.gdx.graphics.Color.ORANGE;
                break;
            case YELLOW:
                newColor = com.badlogic.gdx.graphics.Color.YELLOW;
                break;
            case GREEN:
                newColor = com.badlogic.gdx.graphics.Color.GREEN;
                break;
            case BROWN:
                newColor = com.badlogic.gdx.graphics.Color.BROWN;
                break;
            case PINK:
                newColor = com.badlogic.gdx.graphics.Color.PINK;
                break;
            default:
                newColor = com.badlogic.gdx.graphics.Color.WHITE;
                break;
        }
        pixmap.setColor(newColor);
        com.badlogic.gdx.graphics.Color whiteColor = com.badlogic.gdx.graphics.Color.WHITE;
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                com.badlogic.gdx.graphics.Color pixelColor = new com.badlogic.gdx.graphics.Color(pixmap.getPixel(x, y));
                if (pixelColor.equals(whiteColor)) {
                    pixmap.fillRectangle(x, y, 1, 1);
                }
            }
        }
        image = new Texture(pixmap);
        image.getTextureData().disposePixmap();
        pixmap.dispose();
    }

    public void setMineOnMountain() {
        image = new Texture("mineMountain.png");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
