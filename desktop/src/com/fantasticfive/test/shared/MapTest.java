package com.fantasticfive.test.shared;


import com.fantasticfive.shared.Hexagon;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.Player;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.GroundType;
import com.fantasticfive.shared.enums.ObjectType;
import com.fantasticfive.test.settings.SettingsTest;
import org.junit.Before;
import org.junit.Test;

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
        Player p = new Player("test", Color.RED, 1);
        hex.setOwner(p);

        assertNotNull("Hex should have an owner",hex.getLocation());

        assertNull("Hex at this location should not have an owner",
                map.getHexAtLocation(new Point(2,6)).getOwner());
    }


    @Test
    public void testGetHexAtLocation() throws Exception {
        Point point = new Point(2,2);
        assertNotNull("Map should have returned a hex with location 2,2",
                map.getHexAtLocation(point));

        point = new Point(50,30);
        assertNull("There should not be a hex at location 50, 50",
                map.getHexAtLocation(point));
    }
}