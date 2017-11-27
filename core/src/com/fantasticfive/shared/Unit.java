package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;

import java.io.Serializable;

public class Unit implements Cloneable, Serializable {
    public Texture texture;
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

    public boolean attack(Unit unitToAttack) {
        if (calculateDistance(this.location, unitToAttack.getLocation()) <= attackRange && this.allowedToMove >= 1) {
            unitToAttack.reduceHealth(attackPower - unitToAttack.getArmor());
            allowedToMove = 0;
            return true;
        }
        return false;
    }

    public boolean attack(Building buildingToAttack) {
        if (calculateDistance(this.location, buildingToAttack.getLocation()) <= attackRange && this.allowedToMove >= 1) {
            allowedToMove = 0;
            return buildingToAttack.damageHealth(this.attackPower);
        }
        return false;
    }

    public void reduceHealth(int hp) {
        if (health - hp > 0) {
            health -= hp;
        } else if (health - hp <= 0) {
            health = 0;
        }
        System.out.println("Health has been reduced with " + hp + " to " + health);
    }

    public void move(Point destination) {
        if (allowedToMove - calculateDistance(location, destination) < 0) {
            System.out.println("Not allowed to walk :(");
        } else {
            this.allowedToMove -= calculateDistance(location, destination);
            this.location = destination;
        }
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void toggleSelected() {
        if (isSelected) {
            System.out.println("Unit deselected");
            isSelected = false;
        } else {
            System.out.println("Unit selected");
            isSelected = true;
        }
    }

    public Boolean getSelected() {
        return isSelected;
    }

    //Calculates distance from 2 selected points
    public int calculateDistance(Point p1, Point p2) {
        //TODO This formula isn't 100% correct yet

        p1.z = -(p1.y + p1.x);
        p2.z = -(p2.y + p2.x);

        return (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z)) / 2;
    }

    public int getMovementLeft() {
        return this.allowedToMove;
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
