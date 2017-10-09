package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import javax.xml.soap.Text;
import java.io.File;
import java.util.List;

public class BuildingFactory {
    private List<Building> buildingPresets;

    public BuildingFactory() {
        GroundType[] buildableOn = new GroundType[]{GroundType.GRASS, GroundType.DIRT, GroundType.SAND};
        buildingPresets.add(new Barracks(30, new Texture("barracks.png"), 30, buildableOn));
        buildingPresets.add(new Resource(10, new Texture("mine.png"), 15, 3, buildableOn));
        buildingPresets.add(new Fortification(20, new Texture("tower.png"), 10, buildableOn));
    }

    public Building createBuilding(BuildingType buildingType, Point location, Player player) {
        Building building;
        GroundType[] buildableOn = new GroundType[]{GroundType.GRASS, GroundType.DIRT, GroundType.SAND};
        switch (buildingType){
            case TOWNCENTRE: building = new TownCentre(50, location, new Texture("townCentre.png"), buildableOn, player);
                break;
            case BARRACKS: building = getBuildingPreset(buildingType);
            ((Barracks)building).setCreatableUnits(new UnitType[]{UnitType.SWORDSMAN, UnitType.ARCHER, UnitType.SCOUT});
                break;
            case RESOURCE: building = getBuildingPreset(buildingType);
            ((Resource)building).setProductionPerTurn(3);
                break;
            case FORTIFICATION: building = getBuildingPreset(buildingType);
                break;
            default: building = null;
        }
        building.setLocation(location);
        building.setOwner(player);
        return building;
    }

    public Building getBuildingPreset(BuildingType buildingType) {
        for (Building building : buildingPresets) {
            switch (buildingType) {
                case BARRACKS: if (building instanceof Barracks){
                    return building;
                }
                break;
                case RESOURCE: if (building instanceof Resource){
                    return building;
                }
                break;
                case FORTIFICATION: if (building instanceof Fortification){
                    return building;
                }
                break;
                default: return null;
            }
        }
        return null;
    }
}
