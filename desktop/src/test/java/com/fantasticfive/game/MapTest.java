package com.fantasticfive.game;

import com.fantasticfive.game.enums.Color;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.ObjectType;
import org.junit.*;
import static org.junit.Assert.*;

public class MapTest extends SettingsTest {
    private Map map;

    @Before
    public void initMap() {
        map = new Map(20,20);
    }

    @Test
    public void testHexHasOwner() throws Exception {
        Hexagon hex = map.getHexAtLocation(new Point(1,0));
        Player p = new Player("test", Color.RED);
        hex.setOwner(p);

        Assert.assertNotNull("Hex should have an owner",hex.getLocation());

        Assert.assertNull("Hex at this location should not have an owner",
                map.getHexAtLocation(new Point(2,6)));
    }

    @Test
    public void testIsHexBuildable() throws Exception {
        Player p = new Player("test", Color.RED);

        Hexagon grassHex = null;
        Hexagon mountainHex = null;
        Hexagon waterHex = null;

        //Search for the different hexes
        for(Hexagon h: map.getHexagons()) {
            if(h.getGroundType() == GroundType.GRASS) {
                grassHex = h;
            } else if(h.getGroundType() == GroundType.WATER) {
                waterHex = h;
            }
            if(h.getObjectType() == ObjectType.MOUNTAIN) {
                mountainHex = h;
            }
        }

        Assert.assertFalse("grassHex shouldn't be buildable without owner",
                map.isHexBuildable(grassHex.getLocation(), p));
        Assert.assertFalse("mountainHex should always return false",
                map.isHexBuildable(mountainHex.getLocation(), p));
        Assert.assertFalse("waterHex should always return false",
                map.isHexBuildable(waterHex.getLocation(), p));

        grassHex.setOwner(p);
        mountainHex.setOwner(p);
        waterHex.setOwner(p);

        Assert.assertTrue("grassHex should be buildable with owner",
                map.isHexBuildable(grassHex.getLocation(), p));
        Assert.assertFalse("mountainHex shouldn't be buildable with owner",
                map.isHexBuildable(mountainHex.getLocation(), p));
        Assert.assertFalse("waterHex shouldn't be buildable with owner",
                map.isHexBuildable(waterHex.getLocation(), p));

    }

    @Test
    public void testGetHexAtLocation() throws Exception {
        Point point = new Point(2,2);
        Assert.assertNotNull("Map should have returned a hex with location 2,2",
                map.getHexAtLocation(point));

        point = new Point(50,30);
        Assert.assertNull("There should not be a hex at location 50, 50",
                map.getHexAtLocation(point));
    }
}
