package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

public class CraftDimensionUtil {

    private CraftDimensionUtil() {
    }

    public static ResourceKey<Level> getMainDimensionKey(Level world) {
        ResourceKey<LevelStem> typeKey = world.getTypeKey();
        if (typeKey == LevelStem.OVERWORLD) {
            return Level.OVERWORLD;
        } else if (typeKey == LevelStem.NETHER) {
            return Level.NETHER;
        } else if (typeKey == LevelStem.END) {
            return Level.END;
        }

        return world.dimension();
    }
}
