package com.fantasticfive.shared;

public interface IMap {
    boolean isHexBuildable(Point Location, Player currentPlayer);
    Hexagon getHexAtLocation(Point location);
}
