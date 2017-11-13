package com.fantasticfive.shared.enums;

/**
 * ProjectHex Created by Sven de Vries on 9-10-2017
 */
public enum Color {
    RED,
    BLUE,
    PURPLE,
    ORANGE,
    YELLOW,
    GREEN,
    BROWN,
    PINK;

    public static Color getRandomColor(){ return values()[(int)(Math.random() * values().length)];}
}
