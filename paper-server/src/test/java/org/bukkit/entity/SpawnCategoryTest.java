package org.bukkit.entity;

import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class SpawnCategoryTest {

    @Test
    public void testMatch() {
        for (SpawnCategory category : SpawnCategory.values()) {
            if (CraftSpawnCategory.isValidForLimits(category)) {
                long defaultTicks = CraftSpawnCategory.getDefaultTicksPerSpawn(category);
                String nameConfigSpawnLimit = CraftSpawnCategory.getConfigNameSpawnLimit(category);
                String nameConfigTicksPerSpawn = CraftSpawnCategory.getConfigNameTicksPerSpawn(category);
            }
        }
    }
}
