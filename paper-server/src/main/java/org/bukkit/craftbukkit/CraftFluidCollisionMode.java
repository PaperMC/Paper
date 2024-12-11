package org.bukkit.craftbukkit;

import net.minecraft.world.level.ClipContext.Fluid;
import org.bukkit.FluidCollisionMode;

public final class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static Fluid toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return Fluid.ANY;
            case SOURCE_ONLY:
                return Fluid.SOURCE_ONLY;
            case NEVER:
                return Fluid.NONE;
            default:
                return null;
        }
    }
}
