package com.fantasticfive.shared;

public interface IMap {
    boolean isHexBuildable(Point location, Player currentPlayer);
    Hexagon getHexAtLocation(Point location);
}
