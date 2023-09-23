package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class EntityTypesTest extends AbstractTestingBase {

    @Test
    public void testMaps() {
        Set<EntityType> allBukkit = Arrays.stream(EntityType.values()).filter((b) -> b.getName() != null).collect(Collectors.toSet());

        for (EntityTypes<?> nms : BuiltInRegistries.ENTITY_TYPE) {
            MinecraftKey key = EntityTypes.getKey(nms);

            EntityType bukkit = EntityType.fromName(key.getPath());
            assertNotNull(bukkit, "Missing nms->bukkit " + key);

            assertTrue(allBukkit.remove(bukkit), "Duplicate entity nms->" + bukkit);
        }

        assertTrue(allBukkit.isEmpty(), "Unmapped bukkit entities " + allBukkit);
    }

    @Test
    public void testTranslationKey() {
        for (EntityType entityType : EntityType.values()) {
            // Currently EntityType#getTranslationKey has a validation for null name then for test skip this and check correct names.
            if (entityType.getName() != null) {
                assertNotNull(entityType.getTranslationKey(), "Nulllable translation key for " + entityType);
            }
        }
    }
}
