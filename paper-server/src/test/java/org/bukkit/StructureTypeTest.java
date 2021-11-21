package org.bukkit;

import java.util.Map;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
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
        for (MinecraftKey key : IRegistry.STRUCTURE_FEATURE.keySet()) {
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
    public void testBukkitToMinecraft() {
        for (Map.Entry<String, StructureType> entry : structures.entrySet()) {
            Assert.assertNotNull(entry.getKey(), IRegistry.STRUCTURE_FEATURE.get(new MinecraftKey(entry.getKey())));
            Assert.assertNotNull(entry.getValue().getName(), IRegistry.STRUCTURE_FEATURE.get(new MinecraftKey(entry.getValue().getName())));
        }
    }
}
