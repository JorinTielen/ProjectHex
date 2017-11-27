package com.fantasticfive.server;

import com.fantasticfive.shared.Player;
import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.Unit;
import java.util.ArrayList;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets;

    UnitFactory() {
        Database db = new Database();
        unitPresets = new ArrayList<>();
        unitPresets.add(db.getSwordsmanPreset());
        unitPresets.add(db.getArcherPreset());
        unitPresets.add(db.getScoutPreset());
    }

    Unit createUnit(UnitType unitType, Point location, Player owner) {
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

    Unit getUnitPreset(UnitType unitType) {
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
