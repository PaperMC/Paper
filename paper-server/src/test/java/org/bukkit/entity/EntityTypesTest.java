package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class EntityTypesTest {

    @Test
    public void testTranslationKey() {
        for (org.bukkit.entity.EntityType entityType : org.bukkit.entity.EntityType.values()) {
            // Currently EntityType#getTranslationKey has a validation for null name then for test skip this and check correct names.
            if (entityType.getName() != null) {
                assertNotNull(entityType.getTranslationKey(), "Nulllable translation key for " + entityType);
            }
        }
    }

    @Test
    void testGetSpawnCategory() {
        for (EntityType type : EntityType.values()) {
            if (type == EntityType.UNKNOWN) continue;
            assertNotNull(type.getSpawnCategory(), "SpawnCategory should not be null for " + type);
        }
    }
}
