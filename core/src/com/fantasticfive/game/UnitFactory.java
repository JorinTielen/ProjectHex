package com.fantasticfive.game;

import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.UnitType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets = new ArrayList<Unit>();

    public UnitFactory() {
        fillList();
    }

    private List<Unit> getUnitPresets() {
        return Collections.unmodifiableList(unitPresets);
    }

    public Unit createUnit(UnitType unitType, Point location, Player owner) {
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

    public Unit getUnitPreset(UnitType unitType) {
        Unit copy = null;
        for(Unit unit : getUnitPresets()) {
            if(unit.getType() == unitType) {
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

    private void fillList() {
        unitPresets.add(new Unit(UnitType.SWORDSMAN, 100, 15, 30,
                1, 2, 100, 1,
                false, 150, new Texture("characterSwordsman.png")));
        unitPresets.add(new Unit(UnitType.ARCHER, 75, 5, 30,
                2, 3, 125, 1,
                false, 150, new Texture("characterArcher.png")));
        unitPresets.add(new Unit(UnitType.SCOUT, 50, 0, 15,
                1, 5, 75, 1,
                true, 150, new Texture("characterScout.png")));
    }

}
