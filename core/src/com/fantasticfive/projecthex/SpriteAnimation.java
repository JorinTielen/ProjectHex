package com.fantasticfive.projecthex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.fantasticfive.shared.Building;
import com.fantasticfive.shared.Point;

public class SpriteAnimation {
    private Texture sprite;
    private String spriteName;
    private int frame;
    private int totalAnimationFrames;
    private int totalCount;
    private boolean active;
    private Point location;
    private int loopValue;

    public SpriteAnimation(String spriteName, Point location){
        this.spriteName = spriteName;
        this.sprite = new Texture(spriteName + "1.png");
        this.frame = 0;
        this.totalCount = 0;
        this.totalAnimationFrames = 0;
        this.active = true;
        this.location = location;
        loopValue = 3;
    }

    //Animates seperate images from a gif by changing image every other frame.
    public boolean animate(){
        if (frame % 12 == 0 && totalAnimationFrames == 0){
            try{
                sprite = new Texture(spriteName + (frame / 12) + ".png");
            }
            catch (GdxRuntimeException e){
                totalAnimationFrames = frame - 12;
            }
        }
        else if (frame % 12 == 0){
            if (frame > totalAnimationFrames){
                frame = 0;
            }
            sprite = new Texture(spriteName + (frame / 12) + ".png");
        }
        totalCount++;
        frame++;
        if (totalCount >= totalAnimationFrames * loopValue && totalAnimationFrames != 0){
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

    public Vector2 getPos() {
        double height = 62 * 2;
        double width = height;

        double vert = height * 0.75f;
        double horiz = width;

        float x = (float) (horiz * (this.location.getY() + this.location.getX() / 2f));
        float y = (float) (vert * this.location.getX());

        return new Vector2(x, y);
    }
}
