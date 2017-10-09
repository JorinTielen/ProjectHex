package com.fantasticfive.game;

import com.fantasticfive.game.enums.UnitType;

import java.io.File;

public class Unit {

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
    private File image;
    private int allowedToMove;

    private Player owner;

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, Boolean canTakeLand,
                int upgradeCost, File image) {
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
        this.image = image;
        this.owner = owner;
    }

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, Boolean canTakeLand,
                int upgradeCost, Point location, File image, Player owner) {
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
        this.image = image;
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
}
