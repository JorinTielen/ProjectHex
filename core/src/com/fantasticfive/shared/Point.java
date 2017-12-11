package com.fantasticfive.shared;

import java.io.Serializable;

public class Point implements Serializable {
    public int x;
    public int y;
    public int z;

    // X = r | row
    // Y = q | column
    // Z = s

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = -(y + x);
    }

    public boolean equals(Point other) {
        return this.x == other.x && this.y == other.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point value(){
        return new Point(x,y);
    }

}
