package com.fantasticfive.game;

import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets;

    public UnitFactory() {
        fillList();
    }

    private List<Unit> getUnitPresets() {
        return Collections.unmodifiableList(unitPresets);
    }

    private Unit createUnit(UnitType unitType, Point location, Player owner) {
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
        unit.move(location);
        return unit;
    }

    private Unit getUnitPreset(UnitType unitType) {
        for(Unit unit : getUnitPresets()) {
            if(unit.getType() == unitType) {
                return unit;
            }
        }
        return null;
    }

    private void fillList() {
        unitPresets.add(new Unit(UnitType.SWORDSMAN, 100, 15, 30, 1, 2, 100, 1, false, 150, null));
    }
}
