package org.bukkit;

import java.util.Map;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
    @Ignore("Some types missing during unit test run")
    public void testMinecraftToBukkit() {
        for (MinecraftKey key : IRegistry.STRUCTURE_TYPES.keySet()) {
            Assert.assertNotNull(key.getPath(), structures.get(key.getPath()));
        }
    }

    @Test
    public void testBukkit() {
        for (Map.Entry<String, StructureType> entry : structures.entrySet()) {
            Assert.assertNotNull(entry.getKey(), StructureType.getStructureTypes().get(entry.getKey()));
            Assert.assertNotNull(entry.getValue().getName(), StructureType.getStructureTypes().get(entry.getValue().getName()));
        }
    }

    @Test
    @Ignore("Some types missing during unit test run")
    public void testBukkitToMinecraft() {
        for (Map.Entry<String, StructureType> entry : structures.entrySet()) {
            Assert.assertNotNull(entry.getKey(), IRegistry.STRUCTURE_TYPES.get(new MinecraftKey(entry.getKey())));
            Assert.assertNotNull(entry.getValue().getName(), IRegistry.STRUCTURE_TYPES.get(new MinecraftKey(entry.getValue().getName())));
        }
    }
}
