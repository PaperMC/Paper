package org.bukkit.entity;

import net.minecraft.world.entity.MobCategory;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class SpawnCategoryTest {

    @Test
    public void testMatch() {
        for (MobCategory category : MobCategory.values()) {
            // If it is missing a convert to Bukkit then throw a UnsupportedOperationException
            SpawnCategory spawnCategory = CraftSpawnCategory.toBukkit(category);

            if (CraftSpawnCategory.isValidForLimits(spawnCategory)) {
                long defaultTicks = CraftSpawnCategory.getDefaultTicksPerSpawn(spawnCategory);
                String nameConfigSpawnLimit = CraftSpawnCategory.getConfigNameSpawnLimit(spawnCategory);
                String nameConfigTicksPerSpawn = CraftSpawnCategory.getConfigNameTicksPerSpawn(spawnCategory);
            }
        }

        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            // If it is missing a convert to NMS then throw a UnsupportedOperationException
            MobCategory category = CraftSpawnCategory.toNMS(spawnCategory);
        }
    }
}
