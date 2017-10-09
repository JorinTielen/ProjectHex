package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.ObjectType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

public class Hexagon {
    private GroundType groundType;
    private ObjectType objectType;
    private boolean accessible;
    private Point location;
    private int radius;
    private Texture groundImage;
    private Texture objectImage;
    private Player owner;

    public Hexagon(GroundType groundType, Point location, int radius) {
        this.groundType = groundType;
        this.location = location;
        this.radius = radius;
    }

    public Hexagon(GroundType groundType, ObjectType objectType, Point location, int radius) {
        this.groundType = groundType;
        this.objectType = objectType;
        this.location = location;
        this.radius = radius;
    }

    public Point getLocation() {
        return location;
    }

    public Player getOwner(){
        return owner;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void addObject(BuildingType buildingType){
        addObjectType(ObjectType.BUILDING);
        switch (buildingType){
            case TOWNCENTRE: objectImage = new Texture("townCentre.png");
            break;
            case BARRACKS: objectImage = new Texture("barracks.png");
            break;
            case FORTIFICATION: objectImage = new Texture("tower.png");
            break;
            case RESOURCE: objectImage = new Texture("mine.png");
        }
    }
    public void addObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public void removeObjectType() {
        this.objectType = null;
    }

    public void addOwner(Player owner) {
        this.owner = owner;
    }

    public void deleteOwner() {
        this.owner = null;
    }

    public boolean hasOwner(){
        throw new NotImplementedException();
    }
}
