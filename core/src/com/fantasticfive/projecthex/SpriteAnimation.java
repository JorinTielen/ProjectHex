package com.fantasticfive.projecthex;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.Point;

import java.io.File;

public class SpriteAnimation {
    private Texture sprite;
    private String spriteName;
    private int frame;
    private int totalAnimationFrames;
    private int totalCount;
    private boolean active;
    private Point location;

    public SpriteAnimation(String spriteName, Point location){
        this.spriteName = spriteName;
        this.sprite = new Texture(spriteName + "1.png");
        this.frame = 0;
        this.totalCount = 0;
        this.totalAnimationFrames = 0;
        this.active = true;
        this.location = location;
    }

    //Animates seperate images from a gif by changing image every other frame.
    public boolean animate(){
        if (frame % 2 == 0){
            sprite = new Texture(spriteName + frame + ".png");
            totalCount++;
        }
        //catch exception where texture doesn't exist, set totalAnimationFrames to the frame.
        frame++;
        if (totalCount == totalAnimationFrames * 3){
            sprite.dispose();
            active = false;
            return false;
        }
        return true;
    }

    public Texture getTexture(){
        return this.sprite;
    }

    public Point getLocation(){
        return this.location;
    }

    public boolean getActive(){
        return this.active;
    }
}
