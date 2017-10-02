package com.fantasticfive.game;

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
    private int canTakeLand;
    private int upgradeCost;
    private Point location;
    private File image;

    private Player owner;

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, int canTakeLand,
                int upgradeCost, File image, Player owner) {
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
        this.image = image;
        this.owner = owner;
    }

    public Unit(UnitType unitType, int health, int armor,
                int attackPower, int attackRange, int movementRange,
                int purchaseCost, int costPerTurn, int canTakeLand,
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
        throw new UnsupportedOperationException();
    }

    public void attack(Building buildingToAttack) {
        throw new UnsupportedOperationException();
    }

    public void reduceHealth(int hp) {
        throw new UnsupportedOperationException();
    }

    public void move(Point destination) {
        throw new UnsupportedOperationException();
    }

    public void upgrade() {
        throw new UnsupportedOperationException();
    }
}
