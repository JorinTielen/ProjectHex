package com.fantasticfive.server;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.shared.IPlayer;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets;

    public UnitFactory() {
        unitPresets = new ArrayList<Unit>();
        unitPresets.add(new Unit(UnitType.SWORDSMAN, 100, 10, 35,
                1, 2, 20, 1,
                false, 150));
        unitPresets.add(new Unit(UnitType.ARCHER, 75, 5, 25,
                2, 3, 15, 1,
                false, 150));
        unitPresets.add(new Unit(UnitType.SCOUT, 50, 0, 10,
                1, 4, 10, 1,
                true, 150));
    }

    public Unit createUnit(UnitType unitType, Point location, IPlayer owner) {
        Unit unit = null;
        switch (unitType) {
            case SWORDSMAN:
                unit = getUnitPreset(UnitType.SWORDSMAN);
                break;
            case ARCHER:
                unit = getUnitPreset(UnitType.ARCHER);
                break;
            case SCOUT:
                unit = getUnitPreset(UnitType.SCOUT);
                break;
        }
        unit.setOwner(owner);
        unit.setLocation(location);
        return unit;
    }

    public Unit getUnitPreset(UnitType unitType) {
        Unit copy;
        for(Unit unit : unitPresets) {
            if(unit.getUnitType() == unitType) {
                try {
                    copy = (Unit) unit.clone();
                    return copy;
                } catch (CloneNotSupportedException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
