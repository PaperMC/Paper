package org.bukkit.craftbukkit.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;
import net.minecraft.world.level.dimension.DimensionManager;

public class CraftDimensionUtil {

    private CraftDimensionUtil() {
    }

    public static ResourceKey<World> getMainDimensionKey(World world) {
        ResourceKey<DimensionManager> typeKey = world.getTypeKey();
        if (typeKey == DimensionManager.OVERWORLD_LOCATION) {
            return World.OVERWORLD;
        } else if (typeKey == DimensionManager.NETHER_LOCATION) {
            return World.NETHER;
        } else if (typeKey == DimensionManager.END_LOCATION) {
            return World.END;
        }

        return world.getDimensionKey();
    }
}
