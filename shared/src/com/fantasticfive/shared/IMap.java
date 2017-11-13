package com.fantasticfive.shared;

public interface IMap {
    Boolean isHexBuildable(Point Location, Player currentPlayer);
    Hexagon getHexAtLocation(Point location);
}
