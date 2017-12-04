package com.fantasticfive.test.shared;

import com.fantasticfive.test.shared.enums.GroundType;
import org.junit.*;

import static org.junit.Assert.*;

public class BuildingTest extends SettingsTest {
    private Building building;

    @Before
    public void before() {
        GroundType[] buildableOn = new GroundType[]{GroundType.GRASS, GroundType.DIRT, GroundType.SAND};
        building = new TownCentre(150, buildableOn);
    }

    @After
    public void after() {
        building = null;
    }

    @Test
    public void testKillBuilding() {
        Boolean result = building.damageHealth(15);
        assertFalse("Building should have more than 0 hp", result);

        result = building.damageHealth(200);
        assertTrue("Building should have 0 hp", result);
    }

    @Test
    public void testDamageBuilding() {
        int expected = building.health - 50;
        building.damageHealth(50);
        int actual = building.health;
        assertEquals("Building health should be reduced by 50", expected, actual);

        expected = 0;
        building.damageHealth(999);
        actual = building.health;
        assertEquals("Building health should be 0", expected, actual);
    }

}