package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.*;
import java.util.ArrayList;
import java.util.List;

public class Map implements IMap {
    private List<Hexagon> hexagons;
    private int id;
    private int width;
    private int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        hexagons = new ArrayList<Hexagon>();
        Generate();
    }

    public boolean hexHasOwner(Point location) {
        //TODO Daadwerkelijk checken of de tile een owner heeft
        for (Hexagon h : hexagons) {
            if (h.getLocation() == location) {
                return h.hasOwner();
            }
        }
        return false;
    }

    public void setTextures() {
        for (Hexagon hex : hexagons) {
            hex.setTextures();
        }
    }

    private void Generate() {
        Noise.setSeed(1234); //This value is used to calculate the map
        float scale = 0.10f; //To determine the density
        float[][] noiseValues = Noise.Calc2DNoise(height, width, scale);

        int maxNoise1 = 75;
        int maxNoise2 = 125;
        int maxNoise3 = 150;
        int maxNoise4 = 220;
        int maxNoise5 = 235;
        int maxNoise6 = 255;

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                GroundType gt = GroundType.GRASS;
                ObjectType ot = null;
                if (noiseValues[row][column] >= 0 && noiseValues[row][column] < maxNoise1) {
                    gt = GroundType.WATER;
                }
                if (noiseValues[row][column] >= maxNoise1 && noiseValues[row][column] < maxNoise2) {
                    gt = GroundType.SAND;
                }
                if (noiseValues[row][column] >= maxNoise2 && noiseValues[row][column] < maxNoise3) {
                    gt = GroundType.DIRT;
                }
                if (noiseValues[row][column] >= maxNoise3 && noiseValues[row][column] < maxNoise4) {
                    gt = GroundType.GRASS;
                }
                if (noiseValues[row][column] >= maxNoise4 && noiseValues[row][column] < maxNoise5) {
                    gt = GroundType.FOREST;
                }
                if (noiseValues[row][column] >= maxNoise5 && noiseValues[row][column] < maxNoise6) {
                    gt = GroundType.GRASS;
                    ot = ObjectType.MOUNTAIN;
                }
                if (ot == null) {
                    hexagons.add(new Hexagon(gt, new Point(row, column), 62));
                } else {
                    hexagons.add(new Hexagon(gt, ot, new Point(row, column), 62));
                }
            }
        }
    }

    public List<Hexagon> getHexagons() {
        return hexagons;
    }

    //Check if buildings can be placed on given location
    public boolean isHexBuildable(Point location, Player player) {
        Hexagon hex = getHexAtLocation(location);

        if (hex.getOwner() == player &&
                hex.getGroundType() != GroundType.WATER &&
                hex.getObjectType() != ObjectType.MOUNTAIN) {
            return true;
        }
        return false;
    }

    //Returns hexagon at a specific location
    public Hexagon getHexAtLocation(Point loc) {
        for (Hexagon hex : hexagons) {
            if (hex.getLocation().x == loc.x && hex.getLocation().y == loc.y) {
                return hex;
            }
        }
        return null;
    }
}