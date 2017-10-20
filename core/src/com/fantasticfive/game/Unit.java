package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.UnitType;

public class Unit implements Cloneable{
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
    private Point location;
    private int allowedToMove;
    private Player owner;

    private boolean isSelected = false;

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, Boolean canTakeLand,
                int upgradeCost, Texture image) {
        this.unitType = unitType;
        this.health = health;
        this.armor = armor;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.allowedToMove = movementRange;
        this.purchaseCost = purchaseCost;
        this.costPerTurn = costPerTurn;
        this.canTakeLand = canTakeLand;
        this.upgradeCost = upgradeCost;
        this.texture = image;
    }

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, Boolean canTakeLand,
                int upgradeCost, Point location, Texture image, Player owner) {
        this.unitType = unitType;
        this.health = health;
        this.armor = armor;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
        this.movementRange = movementRange;
        this.purchaseCost = purchaseCost;
        this.costPerTurn = costPerTurn;
        this.canTakeLand = canTakeLand;
        this.upgradeCost = upgradeCost;
        this.location = location;
        this.texture = image;
        this.owner = owner;
    }

    public void attack(Unit unitToAttack) {
        unitToAttack.reduceHealth(attackPower - unitToAttack.getArmor());
    }

    public void attack(Building buildingToAttack) {
        throw new UnsupportedOperationException();
    }

    public void reduceHealth(int hp) {
        if(hp > 0) {
            this.health -= hp;
        }
    }

    public void move(Point destination) {
        this.location = destination;
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

    public UnitType getType() {
        return this.unitType;
    }

    public int getPurchaseCost() {
        return this.purchaseCost;
    }

    public int getCostPerTurn() {
        return this.costPerTurn;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Point getLocation() {
        return this.location;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    public void toggleSelected() {
        if(isSelected) {
            isSelected = false;
        } else {
            isSelected = true;
        }
    }

    public Boolean getSelected() {
        return isSelected;
    }
}
