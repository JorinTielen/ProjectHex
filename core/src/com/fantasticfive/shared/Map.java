package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map implements Serializable {
    private List<Hexagon> hexagons;
    private int id;
    private int width;
    private int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        hexagons = new ArrayList<>();
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

    public Point randomPoint() {
        Random rnd = new Random();
        int i;
        do {
            i = rnd.nextInt(hexagons.size());
        } while (hexagons.get(i).getGroundType() != GroundType.GRASS);
        return hexagons.get(i).getLocation();

    }

    public boolean canMoveTo(Unit u, Point location) {
        List<Hexagon> movableHexes = hexesInCirle(u.getLocation(), u.getMovementLeft());
        for (Hexagon hex : movableHexes) {
            if(hex.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }

    public List<Hexagon> hexesInCirle(Point location, int radius) {
        List<Hexagon> results = new ArrayList<>();
        for (Hexagon hex : hexagons) {
            int distance = distance(location, hex.getLocation());
            if (distance >= -radius &&
                    distance <= radius) {
                results.add(hex);
            }
        }
        return results;
    }

    public int distance(Point a, Point b) {
        return (Math.abs(a.y - b.y) + Math.abs(a.x + a.y - b.x - b.y) + Math.abs(a.x - b.x)) / 2;
    }

    private void Generate() {
        //old seed: 1234
        Noise.setSeed(365); //This value is used to calculate the map
        float scale = 0.10f; //To determine the density
        float[][] noiseValues = Noise.Calc2DNoise(height, width, scale);

        int maxNoise1 = 60;
        int maxNoise2 = 90;
        int maxNoise3 = 120;
        int maxNoise4 = 200;
        int maxNoise5 = 220;
        int maxNoise6 = 250;

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

    public boolean bordersOwnLand(Point location, Player currentPlayer) {
        Hexagon h = getHexAtLocation(new Point(location.x - 1, location.y - 1));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x - 1, location.y ));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x - 1, location.y + 1));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x, location.y - 1));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x, location.y + 1));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x + 1, location.y - 1));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x + 1, location.y));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        h = getHexAtLocation(new Point(location.x + 1, location.y + 1));
        if (h.getOwner() == currentPlayer){
            return true;
        }
        return false;
    }

    //Check if buildings can be placed on given location
    public boolean isHexBuildable(Point location, Player player) {
        Hexagon hex = getHexAtLocation(location);

        return (hex.getOwner() == player &&
                hex.getGroundType() != GroundType.WATER &&
                hex.getObjectType() != ObjectType.MOUNTAIN);
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
