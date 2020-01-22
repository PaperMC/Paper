package org.bukkit.craftbukkit.legacy;

import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

public class EvilTest {

    @Test
    public void testFrom() {
        Assert.assertEquals(Material.LEGACY_STONE, CraftEvil.getMaterial(1));
    }

    @Test
    public void testTo() {
        Assert.assertEquals(1, CraftEvil.getId(Material.LEGACY_STONE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegal() {
        Material.STONE.getId();
    }
}
