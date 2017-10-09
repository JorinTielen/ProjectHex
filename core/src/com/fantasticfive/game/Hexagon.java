package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.ObjectType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Random;

public class Hexagon {
    //hex stuff
    private GroundType groundType;
    private ObjectType objectType;
    private boolean accessible;
    private Point location;
    private int radius;
    private Player owner;

    //data
    public Texture groundImage;
    public Texture objectImage;

    private static final double HEIGHT_MULTIPLIER = Math.sqrt(3) / 2;

    public Hexagon(GroundType groundType, Point location, int radius) {
        Random random = new Random();
        this.groundType = groundType;
        this.location = location;
        this.radius = radius;

        switch (groundType) {
            case GRASS:
                this.groundImage = new Texture("grassClear.png");
                break;
            case DIRT:
                this.groundImage = new Texture("dirtClear.png");
                break;
            case WATER:
                this.groundImage = new Texture("waterClear.png");
                break;
            case SAND:
                int randomNumber = random.nextInt(10);

                switch (randomNumber) {
                    case 2:
                        this.groundImage = new Texture("sandTrees1.png");
                        break;
                    case 7:
                        this.groundImage = new Texture("sandTrees2.png");
                        break;
                    default:
                        this.groundImage = new Texture("sandClear.png");
                        break;
                }

                break;
            case FOREST:
                if (random.nextInt(2) == 1) {
                    this.groundImage = new Texture("grassTrees1.png");
                } else {
                    this.groundImage = new Texture("grassTrees2.png");
                }

                break;
        }

        groundImage.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public Hexagon(GroundType groundType, ObjectType objectType, Point location, int radius) {
        this.groundType = groundType;
        this.objectType = objectType;
        this.location = location;
        this.radius = radius;

        switch (groundType) {
            case GRASS:
                this.groundImage = new Texture("grassClear.png");
                break;
            case DIRT:
                this.groundImage = new Texture("dirtClear.png");
                break;
            case WATER:
                this.groundImage = new Texture("waterClear.png");
                break;
            case SAND:
                this.groundImage = new Texture("sandClear.png");
                break;
            case FOREST:
                this.groundImage = new Texture("grassClear.png");
                break;
        }

        objectImage = new Texture("rockBig.png");
        groundImage.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
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

    //returns the real x,y position of this hex
    public Vector2 getPos() {
        double height = radius * 2;
        double width = height;

        double vert = height * 0.75f;
        double horiz = width;

        float x = (float) (horiz * (this.location.y + this.location.x / 2f));
        float y = (float) (vert * this.location.x);

        return new Vector2(x, y);
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

    public boolean hasOwner() {
        throw new NotImplementedException();
    }
}
