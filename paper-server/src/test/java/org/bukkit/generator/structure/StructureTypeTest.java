package org.bukkit.generator.structure;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

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
            assertNotNull(Registry.STRUCTURE_TYPE.get(NamespacedKey.fromString(name.toLowerCase())), "No enchantment for field name " + name);
        }
    }

    @Test
    public void testMinecraftToBukkitFieldName() {
        for (net.minecraft.world.level.levelgen.structure.StructureType<?> structureType : BuiltInRegistries.STRUCTURE_TYPE) {
            MinecraftKey minecraftKey = BuiltInRegistries.STRUCTURE_TYPE.getKey(structureType);

            try {
                StructureType bukkit = (StructureType) StructureType.class.getField(minecraftKey.getPath().toUpperCase()).get(null);

                assertEquals(minecraftKey, CraftNamespacedKey.toMinecraft(bukkit.getKey()), "Keys are not the same for " + minecraftKey);
            } catch (NoSuchFieldException e) {
                fail("No Bukkit default enchantment for " + minecraftKey);
            } catch (IllegalAccessException e) {
                fail("Bukkit field is not access able for " + minecraftKey);
            } catch (ClassCastException e) {
                fail("Bukkit field is not of type enchantment for" + minecraftKey);
            }
        }
    }
}
