package org.bukkit.entity;

import net.minecraft.world.entity.EnumCreatureType;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.junit.jupiter.api.Test;

public class SpawnCategoryTest {

    @Test
    public void testMatch() {
        for (EnumCreatureType enumCreatureType : EnumCreatureType.values()) {
            // If it is missing a convert to Bukkit then throw a UnsupportedOperationException
            SpawnCategory spawnCategory = CraftSpawnCategory.toBukkit(enumCreatureType);

            if (CraftSpawnCategory.isValidForLimits(spawnCategory)) {
                long defaultTicks = CraftSpawnCategory.getDefaultTicksPerSpawn(spawnCategory);
                String nameConfigSpawnLimit = CraftSpawnCategory.getConfigNameSpawnLimit(spawnCategory);
                String nameConfigTicksPerSpawn = CraftSpawnCategory.getConfigNameTicksPerSpawn(spawnCategory);
            }
        }

        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            // If it is missing a convert to NMS then throw a UnsupportedOperationException
            EnumCreatureType enumCreatureType = CraftSpawnCategory.toNMS(spawnCategory);
        }
    }
}
