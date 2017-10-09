package com.fantasticfive.game;

import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.projecthex.Hex;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<Hexagon> hexagons;
    private int id;

    private int width;
    private int height;

    public Map(int width, int height){
        this.width = width;
        this.height = height;

        hexagons = new ArrayList<Hexagon>();
        Generate();
    }

    public boolean hexHasOwner(Point location){
        throw new NotImplementedException();
    }

    private void Generate() {
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                hexagons.add(new Hexagon(GroundType.GRASS, new Point(row, column), 56));
            }
        }
    }

    public List<Hexagon> getHexagons() {
        return hexagons;
    }
}
