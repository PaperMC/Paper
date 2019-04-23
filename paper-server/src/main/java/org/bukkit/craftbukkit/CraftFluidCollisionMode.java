package org.bukkit.craftbukkit;

import org.bukkit.FluidCollisionMode;
import net.minecraft.server.RayTrace.FluidCollisionOption;

public class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static FluidCollisionOption toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return FluidCollisionOption.ANY;
            case SOURCE_ONLY:
                return FluidCollisionOption.SOURCE_ONLY;
            case NEVER:
                return FluidCollisionOption.NONE;
            default:
                return null;
        }
    }
}
