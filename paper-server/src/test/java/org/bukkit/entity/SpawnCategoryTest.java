package org.bukkit.entity;

import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Normal
public class SpawnCategoryTest {

    @Test
    public void testMatch() {
        for (SpawnCategory category : SpawnCategory.values()) {
            if (CraftSpawnCategory.isValidForLimits(category)) {
                assertDoesNotThrow(() -> CraftSpawnCategory.getDefaultTicksPerSpawn(category));
                assertDoesNotThrow(() -> CraftSpawnCategory.getConfigNameSpawnLimit(category));
                assertDoesNotThrow(() -> CraftSpawnCategory.getConfigNameTicksPerSpawn(category));
            }
        }
    }
}
