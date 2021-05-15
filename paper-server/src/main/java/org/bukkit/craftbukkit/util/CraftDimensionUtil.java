package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;
import net.minecraft.world.level.dimension.DimensionManager;

public class CraftDimensionUtil {

    private CraftDimensionUtil() {
    }

    public static ResourceKey<World> getMainDimensionKey(World world) {
        ResourceKey<DimensionManager> typeKey = world.getTypeKey();
        if (typeKey == DimensionManager.OVERWORLD) {
            return World.OVERWORLD;
        } else if (typeKey == DimensionManager.THE_NETHER) {
            return World.THE_NETHER;
        } else if (typeKey == DimensionManager.THE_END) {
            return World.THE_END;
        }

        return world.getDimensionKey();
    }
}
