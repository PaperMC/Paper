package org.bukkit.generator.structure;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class StructureTest {

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
            assertNotNull(Registry.STRUCTURE.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT))), "No structure for field name " + name);
        }
    }

    @Test
    public void testMinecraftToBukkitFieldName() {
        net.minecraft.core.Registry<net.minecraft.world.level.levelgen.structure.Structure> structureBuiltInRegistries = CraftRegistry.getMinecraftRegistry(Registries.STRUCTURE);
        for (net.minecraft.world.level.levelgen.structure.Structure structure : structureBuiltInRegistries) {
            ResourceLocation minecraftKey = structureBuiltInRegistries.getKey(structure);

            try {
                Structure bukkit = (Structure) Structure.class.getField(minecraftKey.getPath().toUpperCase(Locale.ROOT)).get(null);

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
