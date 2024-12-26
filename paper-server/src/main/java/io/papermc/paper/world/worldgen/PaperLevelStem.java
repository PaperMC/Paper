package io.papermc.paper.world.worldgen;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperLevelStem implements LevelStem, Holderable<net.minecraft.world.level.dimension.LevelStem> {

    public static LevelStem minecraftToBukkit(final net.minecraft.world.level.dimension.LevelStem minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.LEVEL_STEM, RegistryAccess.registryAccess().getRegistry(RegistryKey.LEVEL_STEM));
    }

    public static LevelStem minecraftHolderToBukkit(Holder<net.minecraft.world.level.dimension.LevelStem> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, RegistryAccess.registryAccess().getRegistry(RegistryKey.LEVEL_STEM));
    }

    public static net.minecraft.world.level.dimension.LevelStem bukkitToMinecraft(LevelStem bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.level.dimension.LevelStem> bukkitToMinecraftHolder(LevelStem bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.LEVEL_STEM);
    }

    private final Holder<net.minecraft.world.level.dimension.LevelStem> holder;

    public PaperLevelStem(final Holder<net.minecraft.world.level.dimension.LevelStem> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.world.level.dimension.LevelStem> getHolder() {
        return this.holder;
    }

    @Override
    public DimensionType getDimensionType() {
        return PaperDimensionType.minecraftHolderToBukkit(this.getHandle().type());
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
