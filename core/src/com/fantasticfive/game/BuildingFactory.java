package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class BuildingFactory {

    private List<Building> buildingPresets;

    public BuildingFactory() {

    }

    private Building createBuilding(BuildingType buildingType, Point location) {
        throw new NotImplementedException();
    }

    private Building getBuildingPreset(BuildingType buildingType) {
        throw new NotImplementedException();
    }
}
