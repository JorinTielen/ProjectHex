package com.fantasticfive.shared;

import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.ObjectType;

import java.io.Serializable;
import java.util.*;

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

  public boolean canMoveTo(Unit u, Point location, List<Hexagon> movableHexes) {
        for (Hexagon hex : movableHexes) {
            if(hex.getLocation().sameAs(location)) {
                return true;
            }
        }
        return false;
    }


    public int pathDistance(HashMap pathMap, Hexagon currentHex, Hexagon beginHex){
        Hexagon current = currentHex;
        int i = 0;
        while (current != beginHex){
            current = (Hexagon)pathMap.get(current);
            i++;
        }
        return i;
    }

    public boolean isWithinAttackRange(Unit u, Point location) {
        List<Hexagon> movableHexes = getHexesInRadius(u.getLocation(), u.getAttackRangeLeft());
        for (Hexagon hex : movableHexes) {
            if(hex.getLocation().sameAs(location)) {
                return true;
            }
        }
        return false;
    }

    public List<Hexagon> getHexesInRadius(Point location, int radius) {
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
        return (Math.abs(a.getY() - b.getY()) + Math.abs(a.getX() + a.getY() - b.getX() - b.getY()) + Math.abs(a.getX() - b.getX())) / 2;
    }

    private void Generate() {
        List<Integer> seeds = new ArrayList<>();
        seeds.add(-2096365904);
        seeds.add(-1982397011);
        seeds.add(-1766646759);
        seeds.add(-1742594191);
        seeds.add(-1102120703);
        seeds.add(-970991336);
        seeds.add(-862200254);
        seeds.add(-777100558);
        seeds.add(-516396776);
        seeds.add(-217823742);
        seeds.add(110098218);
        seeds.add(347414893);
        seeds.add(406130710);
        seeds.add(940360477);
        seeds.add(1081319097);
        seeds.add(1138543949);
        seeds.add(1290340836);
        seeds.add(1504742640);
        seeds.add(1551778228);
        seeds.add(1842268213);
        seeds.add(1994802313);
        seeds.add(2004522193);

        Random r = new Random();
        int seed = seeds.get(r.nextInt(seeds.size()));
        Noise.setSeed(seed); //This value is used to calculate the map
        System.out.println(seed);
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
        for (Hexagon h : getHexesInRadius(location, 1)){
            if (h.getOwner() == currentPlayer){
                return true;
            }
        }
        return false;
    }


    public List<Hexagon> getPath(Hexagon startHex, Hexagon destination){
        //TODO Maak gebruik van accessible boolean in Hexagon om te bepalen of je er op kan lopen.
        List<Hexagon> path = new ArrayList<>();
        List<Hexagon> frontier = new ArrayList<>();
        HashMap pathMap = new HashMap();
        boolean found = false;
        Hexagon current;
        int i = 0;
        frontier.add(startHex);
        while (!found){
            current = frontier.get(i);
            if (current == destination){
                found = true;
            }

            for (Hexagon h : getHexesInRadius(current.getLocation(), 1)){
                if (!pathMap.containsKey(h) && h.getObjectType() != ObjectType.MOUNTAIN && h.getGroundType() != GroundType.WATER){
                    frontier.add(h);
                    pathMap.put(h, current);
                }
            }
            i++;
        }

        path.add(destination);
        current = destination;
        while (current != startHex){
            current = (Hexagon)pathMap.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    //Returns hexagon at a specific location
    public Hexagon getHexAtLocation(Point loc) {
        for (Hexagon hex : hexagons) {
            if (hex.getLocation().getX() == loc.getX() && hex.getLocation().getY() == loc.getY()) {
                return hex;
            }
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
