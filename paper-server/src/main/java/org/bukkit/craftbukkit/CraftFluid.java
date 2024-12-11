package org.bukkit.craftbukkit;

import java.util.Locale;
import net.minecraft.core.registries.Registries;
import org.bukkit.Fluid;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;

public class CraftFluid implements Fluid, Handleable<net.minecraft.world.level.material.Fluid> {

    private static int count = 0;

    public static Fluid minecraftToBukkit(net.minecraft.world.level.material.Fluid minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.FLUID, Registry.FLUID);
    }

    public static net.minecraft.world.level.material.Fluid bukkitToMinecraft(Fluid bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final NamespacedKey key;
    private final net.minecraft.world.level.material.Fluid fluidType;
    private final String name;
    private final int ordinal;

    public CraftFluid(NamespacedKey key, net.minecraft.world.level.material.Fluid fluidType) {
        this.key = key;
        this.fluidType = fluidType;
        // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
        // in case plugins use for example the name as key in a config file to receive fluid specific values.
        // Custom fluids will return the key with namespace. For a plugin this should look than like a new fluid
        // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
        if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
            this.name = key.getKey().toUpperCase(Locale.ROOT);
        } else {
            this.name = key.toString();
        }
        this.ordinal = CraftFluid.count++;
    }

    @Override
    public net.minecraft.world.level.material.Fluid getHandle() {
        return this.fluidType;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public int compareTo(@NotNull Fluid fluid) {
        return this.ordinal - fluid.ordinal();
    }

    @NotNull
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public int ordinal() {
        return this.ordinal;
    }

    @Override
    public String toString() {
        // For backwards compatibility
        return this.name();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftFluid otherFluid)) {
            return false;
        }

        return this.getKey().equals(otherFluid.getKey());
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }
}
