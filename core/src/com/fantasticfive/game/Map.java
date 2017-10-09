package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.ObjectType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class Map {
    private List<Hexagon> hexagons;
    private int id;

    public Map(){

    }

    public boolean hexHasOwner(Point location){
        throw new NotImplementedException();
    }

    public boolean isHexBuildable(Point location, Player player){
        Hexagon hex = null;
        for (Hexagon h : hexagons){
            if (h.getLocation() == location){
                hex = h;
            }
        }
        if (hex.getOwner() == player &&
                hex.getObjectType() != ObjectType.MOUNTAIN &&
                hex.getObjectType() != ObjectType.BUILDING){
            return true;
        }
        return false;
    }

    public void createBuilding(BuildingType buildingType, Point location){
        for (Hexagon h : hexagons){
            if (h.getLocation() == location){
                h.addObject(buildingType);
            }
        }
    }
}
