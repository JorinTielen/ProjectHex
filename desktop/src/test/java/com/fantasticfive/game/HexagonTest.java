package com.fantasticfive.game;

import com.fantasticfive.game.enums.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * ProjectHex Created by Sven de Vries on 29-10-2017
 */
public class HexagonTest extends SettingsTest {
    private Hexagon hexagonGrass;
    private Hexagon hexagonWater;

    @Before
    public void initHexagon() {
        hexagonGrass = new Hexagon(GroundType.GRASS, new Point(1,1), 62);
        hexagonWater = new Hexagon(GroundType.WATER, new Point(2,2), 62);
    }

    @Test
    public void testGetPos() throws Exception {
    }

    @Test
    public void testSetOwner() throws Exception {
    }

    @Test
    public void testGetOwner() throws Exception {
    }

    @Test
    public void testGetObjectType() throws Exception {
    }

    @Test
    public void testGetLocation() throws Exception {
    }

    @Test
    public void testAddObjectType() throws Exception {
    }

    @Test
    public void testRemoveObjectType() throws Exception {
    }

    @Test
    public void testRemoveObject() throws Exception {
    }

    @Test
    public void testDeleteOwner() throws Exception {
    }

    @Test
    public void testHasOwner() throws Exception {
    }

    @Test
    public void testGetGroundType() throws Exception {
    }
}