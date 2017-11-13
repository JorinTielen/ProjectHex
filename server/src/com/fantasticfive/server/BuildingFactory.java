package com.fantasticfive.server;

import com.fantasticfive.shared.enums.*;
import com.fantasticfive.shared.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The com.fantasticfive.server.BuildingFactory class contains all presets for buildings and produces instances of these buildings.
 */
public class BuildingFactory {
    private List<Building> buildingPresets;
    private Database db;

    public BuildingFactory() {
        db = new Database();
        buildingPresets = new ArrayList<Building>();
        buildingPresets.add(db.getBarracksPreset());
        buildingPresets.add(db.getResourcePreset());
        buildingPresets.add(db.getFortificationPreset());
        buildingPresets.add(db.getTownCentrePreset());
    }

    /**
     * Create a building for a specific player at the specified location
     * @param buildingType The type of the building
     * @param location The location where the building is to be placed
     * @param player The player who'se buying the building
     * @return
     */
    public Building createBuilding(BuildingType buildingType, Point location, IPlayer player) {
        Building building;
        switch (buildingType){
            case TOWNCENTRE: building = getBuildingPreset(buildingType);
                break;
            case BARRACKS: building = getBuildingPreset(buildingType);
            ((Barracks)building).setCreatableUnits(new UnitType[]{UnitType.SWORDSMAN, UnitType.ARCHER, UnitType.SCOUT});
                break;
            case RESOURCE: building = getBuildingPreset(buildingType);
                break;
            case FORTIFICATION: building = getBuildingPreset(buildingType);
                break;
            default: building = null;
        }
        building.setLocation(location);
        building.setOwner(player);
        return building;
    }

    /**
     * Returns a building of the specified BuildingType
     * @param buildingType The type of building needed
     * @return A building of the specified BuildingType
     */
    public Building getBuildingPreset(BuildingType buildingType) {
        for (Building building : buildingPresets) {
            try {
                switch (buildingType) {
                    case BARRACKS:
                        if (building instanceof Barracks) {
                            return (Building)building.clone();
                        }
                        break;
                    case RESOURCE:
                        if (building instanceof Resource) {
                            return (Building)building.clone();
                        }
                        break;
                    case FORTIFICATION:
                        if (building instanceof Fortification) {
                            return (Building)building.clone();
                        }
                        break;
                    case TOWNCENTRE:
                        if (building instanceof TownCentre) {
                            return (Building)building.clone();
                        }
                        break;
                    default:
                        return null;
                }
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
        return null;
    }
}
