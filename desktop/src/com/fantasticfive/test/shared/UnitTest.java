package com.fantasticfive.test.shared;

import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.test.settings.SettingsTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest extends SettingsTest {
    private Unit unit;

    @Before
    public void initUnit() {
        unit = new Unit(UnitType.SWORDSMAN, 100, 10, 35, 1, 2, 20, 1, false, 150);
        unit.setLocation(new Point(1,1));
    }

    @After
    public void deleteUnit() {
        unit = null;
    }

    @Test
    public void testAttackUnit() {
        Unit enemy = new Unit(UnitType.SWORDSMAN, 100, 10, 35, 1, 2, 20, 1, false, 150);
        enemy.setLocation(new Point(1,2));
        Boolean result = unit.attack(enemy);
        assertTrue("Enemy unit should be within attack range", result);

        unit.resetMoves();

        enemy = new Unit(UnitType.SWORDSMAN, 100, 10, 35, 1, 2, 20, 1, false, 150);
        result = unit.attack(enemy);
        assertFalse("Enemy unit should be outside of the attack range",result);
    }

    @Test
    public void testAttackBuilding() {
        Building building = new TownCentre(150, null);
        building.setLocation(new Point(1,2));
        for(int i = 0; i < 5; i++){
            unit.attack(building);
            unit.resetMoves();
        }
        Boolean result = unit.attack(building);
        assertTrue("Building should be within attack range and is destroyed", result);

        unit.resetMoves();

        building = new TownCentre(150, null);
        building.setLocation(new Point(1,6));
        result = unit.attack(building);
        assertFalse("Building should be outside of the attack range", result);
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
        Player p = new Player("test", Color.RED, 1);
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