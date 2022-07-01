package org.bukkit.generator.structure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class StructureTest extends AbstractTestingBase {

    @Test
    public void testBukkitToMinecraftFieldName() {
        for (Field field : Structure.class.getFields()) {
            if (field.getType() != Structure.class) {
                continue;
            }
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String name = field.getName();
            Assert.assertNotNull("No structure for field name " + name, Registry.STRUCTURE.get(NamespacedKey.fromString(name.toLowerCase())));
        }
    }

    @Test
    public void testMinecraftToBukkitFieldName() {
        IRegistry<net.minecraft.world.level.levelgen.structure.Structure> structureIRegistry = AbstractTestingBase.REGISTRY_CUSTOM.registryOrThrow(IRegistry.STRUCTURE_REGISTRY);
        for (net.minecraft.world.level.levelgen.structure.Structure structure : structureIRegistry) {
            MinecraftKey minecraftKey = structureIRegistry.getKey(structure);

            try {
                Structure bukkit = (Structure) Structure.class.getField(minecraftKey.getPath().toUpperCase()).get(null);

                Assert.assertEquals("Keys are not the same for " + minecraftKey, minecraftKey, CraftNamespacedKey.toMinecraft(bukkit.getKey()));
            } catch (NoSuchFieldException e) {
                Assert.fail("No Bukkit default structure for " + minecraftKey);
            } catch (IllegalAccessException e) {
                Assert.fail("Bukkit field is not access able for " + minecraftKey);
            } catch (ClassCastException e) {
                Assert.fail("Bukkit field is not of type structure for" + minecraftKey);
            }
        }
    }
}
