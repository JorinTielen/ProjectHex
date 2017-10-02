package com.fantasticfive.game;

import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.ObjectType;
import javafx.scene.paint.Color;

import java.io.File;

public class Hexagon {
    private GroundType groundType;
    private ObjectType objectType;
    private boolean accessible;
    private Point location;
    private int radius;
    private File groundImage;
    private File objectImage;
    private Color ownerColor;

    public Hexagon(GroundType groundType, ObjectType objectType, Point location, File groundImage, File objectImage, int radius) {
        this.groundType = groundType;
        this.objectType = objectType;
        this.location = location;
        this.groundImage = groundImage;
        this.objectImage = objectImage;
        this.radius = radius;
    }
}
