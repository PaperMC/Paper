package org.bukkit.generator.structure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class StructureTypeTest extends AbstractTestingBase {

    @Test
    public void testBukkitToMinecraftFieldName() {
        for (Field field : StructureType.class.getFields()) {
            if (field.getType() != StructureType.class) {
                continue;
            }
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            String name = field.getName();
            Assert.assertNotNull("No structure type for field name " + name, Registry.STRUCTURE_TYPE.get(NamespacedKey.fromString(name.toLowerCase())));
        }
    }

    @Test
    public void testMinecraftToBukkitFieldName() {
        for (net.minecraft.world.level.levelgen.structure.StructureType<?> structureType : BuiltInRegistries.STRUCTURE_TYPE) {
            MinecraftKey minecraftKey = BuiltInRegistries.STRUCTURE_TYPE.getKey(structureType);

            try {
                StructureType bukkit = (StructureType) StructureType.class.getField(minecraftKey.getPath().toUpperCase()).get(null);

                Assert.assertEquals("Keys are not the same for " + minecraftKey, minecraftKey, CraftNamespacedKey.toMinecraft(bukkit.getKey()));
            } catch (NoSuchFieldException e) {
                Assert.fail("No Bukkit default structure type for " + minecraftKey);
            } catch (IllegalAccessException e) {
                Assert.fail("Bukkit field is not access able for " + minecraftKey);
            } catch (ClassCastException e) {
                Assert.fail("Bukkit field is not of type structure type for" + minecraftKey);
            }
        }
    }
}
