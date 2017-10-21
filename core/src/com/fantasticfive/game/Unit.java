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
    private Point location = new Point(0,0);
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
        if(calculateDistance(this.location, unitToAttack.location) <= attackRange) {
            unitToAttack.reduceHealth(attackPower - unitToAttack.getArmor());
        } else {
            toggleSelected();
        }
    }

    public void attack(Building buildingToAttack) {
        throw new UnsupportedOperationException();
    }

    public void reduceHealth(int hp) {
        if(health - hp > 0) {
            health -= hp;
        } else if(health - hp <= 0) {
            health = 0;
        }
        System.out.println("Health has been reduced by " + hp + " to " + health);
    }

    public void move(Point destination) {
        if(allowedToMove - calculateDistance(location, destination) < 0) {
            System.out.println("Not allowed to walk :(");
        } else {
            //TODO uncomment this line to lower the amount of tiles a unit is allowed to walk after turns have been implemented
            //this.allowedToMove -= calculateDistance(location, destination);
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

    private int calculateDistance(Point p1, Point p2) {
        //TODO This formula isn't 100% correct yet

        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;

        if(dx * dy > 0) {
            return Math.abs(dx)+Math.abs(dy);
        } else {
            return Math.max(Math.abs(dx),Math.abs(dy));
        }
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getHealth() {
        return this.health;
    }

    public Player getOwner() {
        return owner;
    }
}
