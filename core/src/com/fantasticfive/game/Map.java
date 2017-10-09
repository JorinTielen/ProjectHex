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

        hexagons = new ArrayList<>();
        Generate();
    }

    public boolean hexHasOwner(Point location){
        throw new NotImplementedException();
    }

    private void Generate() {
        Noise.setSeed(1234); //This value is used to calculate the map
        float scale = 0.10f; //To determine the density
        float[][] noiseValues = Noise.Calc2DNoise(height, width, scale);

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                GroundType gt = GroundType.GRASS;
                if (noiseValues[row][column] >= 0   && noiseValues[row][column] < 100) { gt = GroundType.GRASS; }
                if (noiseValues[row][column] >= 100 && noiseValues[row][column] < 150) { gt = GroundType.DIRT; }
                if (noiseValues[row][column] >= 150 && noiseValues[row][column] < 200) { gt = GroundType.SAND; }
                if (noiseValues[row][column] >= 200 && noiseValues[row][column] < 255) { gt = GroundType.WATER; }
                hexagons.add(new Hexagon(gt, new Point(row, column), 62));
            }
        }
    }

    public List<Hexagon> getHexagons() {
        return hexagons;
    }
}
