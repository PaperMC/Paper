package org.bukkit.generator.structure;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

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
            assertNotNull(Registry.STRUCTURE.get(NamespacedKey.fromString(name.toLowerCase())), "No structure for field name " + name);
        }
    }

    @Test
    public void testMinecraftToBukkitFieldName() {
        IRegistry<net.minecraft.world.level.levelgen.structure.Structure> structureBuiltInRegistries = AbstractTestingBase.REGISTRY_CUSTOM.registryOrThrow(Registries.STRUCTURE);
        for (net.minecraft.world.level.levelgen.structure.Structure structure : structureBuiltInRegistries) {
            MinecraftKey minecraftKey = structureBuiltInRegistries.getKey(structure);

            try {
                Structure bukkit = (Structure) Structure.class.getField(minecraftKey.getPath().toUpperCase()).get(null);

                assertEquals(minecraftKey, CraftNamespacedKey.toMinecraft(bukkit.getKey()), "Keys are not the same for " + minecraftKey);
            } catch (NoSuchFieldException e) {
                fail("No Bukkit default structure for " + minecraftKey);
            } catch (IllegalAccessException e) {
                fail("Bukkit field is not access able for " + minecraftKey);
            } catch (ClassCastException e) {
                fail("Bukkit field is not of type structure for" + minecraftKey);
            }
        }
    }
}
