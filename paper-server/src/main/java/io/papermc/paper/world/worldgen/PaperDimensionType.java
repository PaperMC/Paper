package io.papermc.paper.world.worldgen;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperDimensionType implements DimensionType, Holderable<net.minecraft.world.level.dimension.DimensionType> {

    public static DimensionType minecraftToBukkit(final net.minecraft.world.level.dimension.DimensionType minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.DIMENSION_TYPE, RegistryAccess.registryAccess().getRegistry(RegistryKey.DIMENSION_TYPE));
    }

    public static DimensionType minecraftHolderToBukkit(Holder<net.minecraft.world.level.dimension.DimensionType> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, RegistryAccess.registryAccess().getRegistry(RegistryKey.DIMENSION_TYPE));
    }

    public static net.minecraft.world.level.dimension.DimensionType bukkitToMinecraft(DimensionType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.level.dimension.DimensionType> bukkitToMinecraftHolder(DimensionType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.DIMENSION_TYPE);
    }

    private final Holder<net.minecraft.world.level.dimension.DimensionType> holder;

    public PaperDimensionType(final Holder<net.minecraft.world.level.dimension.DimensionType> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.world.level.dimension.DimensionType> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
