package org.bukkit.craftbukkit.legacy;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.Material;
import org.bukkit.support.environment.Legacy;
import org.junit.jupiter.api.Test;

@Legacy
public class EvilTest {

    @Test
    public void testFrom() {
        assertEquals(Material.LEGACY_STONE, CraftEvil.getMaterial(1));
    }

    @Test
    public void testTo() {
        assertEquals(1, CraftEvil.getId(Material.LEGACY_STONE));
    }

    @Test
    public void testIllegal() {
        assertThrows(IllegalArgumentException.class, () -> Material.STONE.getId());
    }
}
