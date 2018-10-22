package org.bukkit;

import java.util.Map;
import net.minecraft.server.WorldGenFactory;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test makes sure that Bukkit always has Minecraft structure types up to
 * date.
 */
public class StructureTypeTest extends AbstractTestingBase {

    private static Map<String, StructureType> structures;

    @BeforeClass
    public static void setUp() {
        structures = StructureType.getStructureTypes();
    }

    @Test
    public void testMinecraftToBukkit() {
        for (String key : WorldGenFactory.structureStartMap.keySet()) {
            Assert.assertNotNull(structures.get(key));
        }
    }

    @Test
    public void testBukkit() {
        for (Map.Entry<String, StructureType> entry : structures.entrySet()) {
            Assert.assertNotNull(StructureType.getStructureTypes().get(entry.getKey()));
            Assert.assertNotNull(StructureType.getStructureTypes().get(entry.getValue().getName()));
        }
    }

    @Test
    public void testBukkitToMinecraft() {
        for (Map.Entry<String, StructureType> entry : structures.entrySet()) {
            Assert.assertNotNull(WorldGenFactory.structureStartMap.get(entry.getKey()));
            Assert.assertNotNull(WorldGenFactory.structureStartMap.get(entry.getValue().getName()));
        }
    }
}
