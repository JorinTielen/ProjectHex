
package com.fantasticfive.game;

import org.junit.Assert;
import org.junit.Test;

public class MapTest extends GameTest{
    @Test
    public void hexHasOwner() throws Exception {
        Map map = new Map(10,10);
        Assert.assertNotNull(map);
    }

    @Test
    public void getHexagons() throws Exception {
    }

    @Test
    public void isHexBuildable() throws Exception {
    }

    @Test
    public void getHexAtLocation() throws Exception {
    }

}
