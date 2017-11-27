package com.fantasticfive.shared;

import java.io.Serializable;
import java.util.List;

public interface IMap extends Serializable {
    boolean isHexBuildable(Point location, Player currentPlayer);
    Hexagon getHexAtLocation(Point location);
    List<Hexagon> getHexagons();
}
