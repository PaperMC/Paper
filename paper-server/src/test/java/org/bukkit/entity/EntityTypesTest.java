package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class EntityTypesTest {

    @Test
    public void testMaps() {
        Set<EntityType> allBukkit = Arrays.stream(EntityType.values()).filter((b) -> b.getName() != null).collect(Collectors.toSet());

        for (net.minecraft.world.entity.EntityType<?> nms : BuiltInRegistries.ENTITY_TYPE) { // Paper - remap fix
            ResourceLocation key = net.minecraft.world.entity.EntityType.getKey(nms); // Paper - remap fix

            org.bukkit.entity.EntityType bukkit = org.bukkit.entity.EntityType.fromName(key.getPath());
            assertNotNull(bukkit, "Missing nms->bukkit " + key);

            assertTrue(allBukkit.remove(bukkit), "Duplicate entity nms->" + bukkit);
        }

        assertTrue(allBukkit.isEmpty(), "Unmapped bukkit entities " + allBukkit);
    }

    @Test
    public void testTranslationKey() {
        for (org.bukkit.entity.EntityType entityType : org.bukkit.entity.EntityType.values()) {
            // Currently EntityType#getTranslationKey has a validation for null name then for test skip this and check correct names.
            if (entityType.getName() != null) {
                assertNotNull(entityType.getTranslationKey(), "Nulllable translation key for " + entityType);
            }
        }
    }
}
