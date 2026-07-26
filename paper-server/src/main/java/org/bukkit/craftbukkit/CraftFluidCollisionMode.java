package org.bukkit.craftbukkit;

import net.minecraft.world.level.ClipContext.Fluid;
import org.bukkit.FluidCollisionMode;

public final class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static Fluid toFluid(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        return switch (fluidCollisionMode) {
            case ALWAYS -> Fluid.ANY;
            case SOURCE_ONLY -> Fluid.SOURCE_ONLY;
            case NEVER -> Fluid.NONE;
        };
    }
}
