package com.fantasticfive.game;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets;

    public UnitFactory(List<Unit> unitPresets) {
        this.unitPresets = unitPresets;
    }

    private List<Unit> getUnitPresets() {
        return Collections.unmodifiableList(unitPresets);
    }

    private Unit createUnit(UnitType unitType, Point location) {
        throw new NotImplementedException();
    }

    private Unit getUnitPreset(UnitType unitType) {
        throw new NotImplementedException();
    }
}
