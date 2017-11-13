import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;

import java.util.ArrayList;
import java.util.List;

/**
 * The BuildingFactory class contains all presets for buildings and produces instances of these buildings.
 */
public class BuildingFactory {
    private List<Building> buildingPresets;

    public BuildingFactory() {
        buildingPresets = new ArrayList<Building>();
        GroundType[] buildableOn = new GroundType[]{GroundType.GRASS, GroundType.DIRT, GroundType.SAND};
        buildingPresets.add(new Barracks(125, new Texture("barracks.png"), 30, buildableOn));
        buildingPresets.add(new Resource(100, new Texture("mine.png"), 15, 3, buildableOn));
        buildingPresets.add(new Fortification(150, new Texture("tower.png"), 10, buildableOn));
    }

    /**
     * Create a building for a specific player at the specified location
     * @param buildingType The type of the building
     * @param location The location where the building is to be placed
     * @param player The player who'se buying the building
     * @return
     */
    public Building createBuilding(BuildingType buildingType, Point location, Player player) {
        Building building;
        GroundType[] buildableOn = new GroundType[]{GroundType.GRASS, GroundType.DIRT, GroundType.SAND};
        switch (buildingType){
            case TOWNCENTRE: building = new TownCentre(200, location, new Texture("townCentre.png"), buildableOn, player);
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
