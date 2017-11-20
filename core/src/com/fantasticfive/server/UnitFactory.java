package com.fantasticfive.server;

import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.shared.IPlayer;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.Unit;
import java.util.ArrayList;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets;

    public UnitFactory() {
        Database db = new Database();
        unitPresets = new ArrayList<Unit>();
        unitPresets.add(db.getSwordsmanPreset());
        unitPresets.add(db.getArcherPreset());
        unitPresets.add(db.getScoutPreset());
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
        for (Unit unit : unitPresets) {
            if (unit.getUnitType() == unitType) {
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
