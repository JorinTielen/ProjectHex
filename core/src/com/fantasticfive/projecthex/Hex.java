package com.fantasticfive.projecthex;

import com.badlogic.gdx.math.Vector2;

public class Hex {
    private int Q;
    private int R;
    private int S;

    private int radius;

    private static final double HEIGHT_MULTIPLIER = Math.sqrt(3) / 2;

    public Hex(int radius, int q, int r) {
        this.Q = q;     //collumn
        this.R = r;     //row
        this.S = -(q + r);

        this.radius = radius;
    }

    //returns the real x,y position of this hex
    public Vector2 getPos() {
        double height = radius * 2;
        double width = HEIGHT_MULTIPLIER * height;

        double vert = height * 0.75f;
        double horiz = width;

        float x = (float)(horiz * (this.Q + this.R/2f));
        float y = (float)(vert * R);

        return new Vector2(x, y);
    }
}
