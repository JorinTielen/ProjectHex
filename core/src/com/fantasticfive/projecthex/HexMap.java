package com.fantasticfive.projecthex;

import java.util.ArrayList;

public class HexMap {
    private int width;
    private int height;

    private ArrayList<Hex> hexes;

    public HexMap(int width, int height) {
        this.width = width;
        this.height = height;

        hexes = new ArrayList<Hex>();
        Generate();
    }

    public ArrayList<Hex> getHexes() {
        return hexes;
    }

    private void Generate() {
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                hexes.add(new Hex(56, column, row));
            }
        }
    }
}
