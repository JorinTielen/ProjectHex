package com.fantasticfive.game;

import com.fantasticfive.game.enums.UnitType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Collections;
import java.util.List;

public class UnitFactory {
    private List<Unit> unitPresets;

    public UnitFactory() {
        //Set presets from database
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
