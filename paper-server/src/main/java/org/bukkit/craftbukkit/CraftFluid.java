package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FluidType;
import org.bukkit.Fluid;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;

public class CraftFluid {

    public static Fluid minecraftToBukkit(FluidType minecraft) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<FluidType> registry = CraftRegistry.getMinecraftRegistry(Registries.FLUID);
        Fluid bukkit = Registry.FLUID.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static FluidType bukkitToMinecraft(Fluid bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.FLUID)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }
}
