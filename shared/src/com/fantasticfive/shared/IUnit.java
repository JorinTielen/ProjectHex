package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.UnitType;

import java.io.Serializable;

public interface IUnit extends Serializable {
    Boolean getSelected();
    Point getLocation();
    void resetMoves();
    void toggleSelected();
    int getCostPerTurn();
    int getPurchaseCost();
    Texture getTexture();
    IPlayer getOwner();
    void move(Point destination);
    boolean attack(IBuilding building);
    boolean attack(IUnit unit);
    int getHealth();
    void reduceHealth(int hp);
    int getArmor();
    UnitType getUnitType();
}
