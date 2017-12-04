package com.fantasticfive.shared;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.ObjectType;

import java.io.Serializable;
import java.util.Random;

public class Hexagon implements Serializable {
    private transient Texture colorCoding;
    private transient Texture groundImage;
    private transient Texture objectImage;
    private GroundType groundType;
    private ObjectType objectType;
    private boolean accessible;
    private Point location;
    private int radius;
    private Player owner;
    private boolean isMountain;
    private static final double HEIGHT_MULTIPLIER = Math.sqrt(3) / 2;

    //Normal hexagon
    public Hexagon(GroundType groundType, Point location, int radius) {
        this.groundType = groundType;
        this.location = location;
        this.radius = radius;
    }

    //Hexagon with rock
    public Hexagon(GroundType groundType, ObjectType objectType, Point location, int radius) {
        this.groundType = groundType;
        this.objectType = objectType;
        this.location = location;
        this.radius = radius;

        if (objectType == ObjectType.MOUNTAIN){
            this.isMountain = true;
        }
    }

    public Texture getColorCoding() {
        return colorCoding;
    }

    public Texture getGroundImage() {
        return groundImage;
    }

    public Texture getObjectImage() {
        return objectImage;
    }

    public void setTextures() {
        Random random = new Random();

        if (objectType == null) {
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
        } else {
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
        }
        setColorTexture();

        groundImage.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void setColorTexture() {
        if(owner != null){
            this.colorCoding = new Texture("terrainColor.png");
            setColor();
        }
    }

    //returns the real x,y position of this hex
    public Vector2 getPos() {
        double height = radius * 2;
        double width = height;

        double vert = height * 0.75f;
        double horiz = width;

        float x = (float) (horiz * (this.location.getY() + this.location.getX() / 2f));
        float y = (float) (vert * this.location.getX());

        return new Vector2(x, y);
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public boolean getIsMountain() {
        return this.isMountain;
    }

    public void setMountain(){
        this.objectType = ObjectType.MOUNTAIN;
        this.objectImage = new Texture("rockBig.png");
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public Point getLocation() {
        return location;
    }

    public void addObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public void removeObjectType() {
        this.objectType = null;
    }

    public void removeObject() {
        this.objectImage = null;
    }

    public void deleteOwner() {
        this.owner = null;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public GroundType getGroundType() {
        return groundType;
    }

    public void setColor(){
        colorCoding.getTextureData().prepare();
        Pixmap pixmap = colorCoding.getTextureData().consumePixmap();
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
        colorCoding = new Texture(pixmap);
        colorCoding.getTextureData().disposePixmap();
        pixmap.dispose();
    }
}
