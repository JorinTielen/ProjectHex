package com.fantasticfive.game;

import com.fantasticfive.game.enums.BuildingType;
import com.fantasticfive.game.enums.Color;
import com.fantasticfive.game.enums.UnitType;
import org.junit.*;
import static org.junit.Assert.*;

public class UnitTest extends SettingsTest {
    private Unit unit;
    private UnitFactory unitFactory;

    @Before
    public void initUnit() {
        unitFactory = new UnitFactory();
        unit = unitFactory.createUnit(UnitType.SWORDSMAN, new Point(1,1), null);
    }

    @After
    public void deleteUnit() {
        unit = null;
        unitFactory = null;
    }

    @Test
    public void testAttackUnit() {
        Unit enemy = unitFactory.createUnit(UnitType.SWORDSMAN, new Point(1,2), null);
        Boolean result = unit.attack(enemy);
        assertTrue("Enemy unit should be within attack range", result);

        unit.resetMoves();

        enemy = unitFactory.createUnit(UnitType.SWORDSMAN, new Point(5,5), null);
        result = unit.attack(enemy);
        assertFalse("Enemy unit should be outside of the attack range",result);
    }

    @Test
    public void testAttackBuilding() {
        BuildingFactory buildingFactory = new BuildingFactory();

        Building building = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(1,2), null);
        Boolean result = unit.attack(building);
        assertFalse("Building should be within attack range", result);

        unit.resetMoves();

        building = buildingFactory.createBuilding(BuildingType.TOWNCENTRE, new Point(5,5), null);
        result = unit.attack(building);
        assertNull("Building should be outside of the attack range", result);
    }

    @Test
    public void testMove() {
        unit.move(new Point(2,2));
        Point result = unit.getLocation();
        Point expected = new Point(2,2);
        assertTrue("New Point should be within movement range",result.equals(expected));

        unit.resetMoves();

        unit.move(new Point(6,8));
        result = unit.getLocation();
        expected = new Point(1,2);
        assertFalse("New location should not be within movement range", result.equals(expected));
    }

    @Test
    public void testUpgrade() {

    }

    @Test
    public void testResetMoves() {
        unit.move(new Point(2, 2));
        unit.resetMoves();
        int expectedMoves = 2;
        int actualMoves = unit.getMovementLeft();
        assertEquals(expectedMoves, actualMoves);
    }

    @Test
    public void testSetOwner() {
        Player p = new Player("test", Color.RED);
        unit.setOwner(p);
        assertTrue(unit.getOwner().getUsername().equals(p.getUsername()));
    }

    @Test
    public void testToggleSelected() {
        unit.toggleSelected();
        assertTrue(unit.getSelected());
    }

    @Test
    public void testCalculateDistance() {
        Point p1 = unit.getLocation();
        Point p2 = new Point(2,1);
        int expectedDistance = 1;
        int actualDistance = unit.calculateDistance(p1,p2);
        assertEquals(expectedDistance, actualDistance);
    }
}
