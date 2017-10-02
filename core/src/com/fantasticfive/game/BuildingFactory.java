package com.fantasticfive.game;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class BuildingFactory {

    private List<Building> buildingPresets;

    public BuildingFactory(List<Building> buildingPresets) {
        this.buildingPresets = buildingPresets;
    }

    private Building createBuilding(BuildingType buildingType, Point location) {
        throw new NotImplementedException();
    }

    private Building getBuildingPreset(BuildingType buildingType) {
        throw new NotImplementedException();
    }
}
