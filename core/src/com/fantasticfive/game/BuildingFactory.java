package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.List;

public class BuildingFactory {
    private List<Building> buildingPresets;

    public BuildingFactory() {
        //Set presets from database
    }

    public Building createBuilding(BuildingType buildingType, Point location) {
        throw new NotImplementedException();
    }

    public Building getBuildingPreset(BuildingType buildingType) {
        throw new NotImplementedException();
    }
}
