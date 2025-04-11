package org.bukkit.craftbukkit;

import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.Fluid;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftFluid extends OldEnumHolderable<Fluid, net.minecraft.world.level.material.Fluid> implements Fluid {

    private static int count = 0;

    public static Fluid minecraftToBukkit(net.minecraft.world.level.material.Fluid minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.FLUID);
    }

    public static net.minecraft.world.level.material.Fluid bukkitToMinecraft(Fluid bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public CraftFluid(final Holder<net.minecraft.world.level.material.Fluid> holder) {
        super(holder, count++);
    }
}
