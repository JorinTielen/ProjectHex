package com.fantasticfive.shared;

import java.io.Serializable;

public interface IMap extends Serializable{
    boolean isHexBuildable(Point location, Player currentPlayer);
    Hexagon getHexAtLocation(Point location);
}
