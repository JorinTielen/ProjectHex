package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.UnitType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Unit implements Cloneable, Serializable {
    private static final Logger LOGGER = Logger.getLogger( Unit.class.getName() );

    private transient Texture texture;
    private UnitType unitType;
    private int health;
    private int armor;
    private int attackPower;
    private int attackRange;
    private int movementRange;
    private int purchaseCost;
    private int costPerTurn;
    private Boolean canTakeLand;
    private int upgradeCost;
    private Point location = new Point(0, 0);
    private int allowedToMove;
    private Player owner;
    private int maxHealth;

    //List for Hexes that the unit can walk on
    private List<Hexagon> walkableHexes;

    private boolean isSelected = false;

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, Boolean canTakeLand,
                int upgradeCost) {
        this.unitType = unitType;
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.allowedToMove = movementRange;
        this.purchaseCost = purchaseCost;
        this.costPerTurn = costPerTurn;
        this.canTakeLand = canTakeLand;
        this.upgradeCost = upgradeCost;
    }

    public void attack(Unit unitToAttack) {
        unitToAttack.reduceHealth(attackPower - unitToAttack.getArmor());
        allowedToMove = 0;
    }

    public boolean attack(Building buildingToAttack) {
        allowedToMove = 0;
        return buildingToAttack.damageHealth(this.attackPower);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void reduceHealth(int hp) {
        health -= hp;
        LOGGER.info("Health has been reduced with " + hp + " to " + health);
        if (health < 0) {
            owner.removeUnit(this);
            LOGGER.info("Unit has died");
        }
    }

    public void setWalkableHexes(List<Hexagon> hexes){
        this.walkableHexes = new ArrayList<>();
        this.walkableHexes = hexes;
    }

    public List<Hexagon> getWalkableHexes(){
        return this.walkableHexes;
    }

    public void move(Point destination, int distance) {
        this.location = destination;
        allowedToMove -= distance;
    }

    public void upgrade() {
        throw new UnsupportedOperationException();
    }

    public void resetMoves() {
        this.allowedToMove = this.movementRange;
    }

    public int getArmor() {
        return this.armor;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public int getPurchaseCost() {
        return this.purchaseCost;
    }

    public int getCostPerTurn() {
        return this.costPerTurn;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return this.location;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {return this.maxHealth;}

    public UnitType getUnitType() {
        return this.unitType;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public int getMovementLeft() {
        return this.allowedToMove;
    }

    public int getAttackRange(){
        return attackRange;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void toggleSelected() {
        if (isSelected) {
            LOGGER.info("Unit deselected");
            isSelected = false;
        } else {
            LOGGER.info("Unit selected");
            isSelected = true;
        }
    }

    public void setTexture() {
        switch (unitType) {
            case ARCHER:
                this.texture = new Texture("characterArcher.png");
                break;
            case SCOUT:
                this.texture = new Texture("characterScout.png");
                break;
            case SWORDSMAN:
                this.texture = new Texture("characterSwordsman.png");
                break;
        }
        setColor();
    }

    public Texture getTexture() {
        return texture;
    }

    public void claimedLand(){
        this.allowedToMove = 0;
    }

    public void setColor(){
        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        com.badlogic.gdx.graphics.Color newColor;
        switch(owner.getColor()){
            case RED: newColor = com.badlogic.gdx.graphics.Color.RED;
                break;
            case BLUE: newColor = com.badlogic.gdx.graphics.Color.BLUE;
                break;
            case PURPLE: newColor = com.badlogic.gdx.graphics.Color.PURPLE;
                break;
            case ORANGE: newColor = com.badlogic.gdx.graphics.Color.ORANGE;
                break;
            case YELLOW: newColor = com.badlogic.gdx.graphics.Color.YELLOW;
                break;
            case GREEN: newColor = com.badlogic.gdx.graphics.Color.GREEN;
                break;
            case BROWN: newColor = com.badlogic.gdx.graphics.Color.BROWN;
                break;
            case PINK: newColor = com.badlogic.gdx.graphics.Color.PINK;
                break;
            default: newColor = com.badlogic.gdx.graphics.Color.WHITE;
                break;
        }
        pixmap.setColor(newColor);
        com.badlogic.gdx.graphics.Color whiteColor = com.badlogic.gdx.graphics.Color.WHITE;
        for (int y = 0; y < pixmap.getHeight(); y++){
            for (int x = 0; x < pixmap.getWidth(); x++){
                com.badlogic.gdx.graphics.Color pixelColor = new com.badlogic.gdx.graphics.Color(pixmap.getPixel(x, y));
                if (pixelColor.equals(whiteColor)){
                    pixmap.fillRectangle(x, y, 1, 1);
                }
            }
        }
        texture = new Texture(pixmap);
        texture.getTextureData().disposePixmap();
        pixmap.dispose();
    }
}
