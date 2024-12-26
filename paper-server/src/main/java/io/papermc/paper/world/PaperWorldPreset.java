package io.papermc.paper.world;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;

public final class PaperWorldPreset implements WorldPreset, Holderable<net.minecraft.world.level.levelgen.presets.WorldPreset> {

    public static WorldPreset minecraftToBukkit(final net.minecraft.world.level.levelgen.presets.WorldPreset minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.WORLD_PRESET, RegistryAccess.registryAccess().getRegistry(RegistryKey.WORLD_PRESET));
    }

    public static WorldPreset minecraftHolderToBukkit(Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, RegistryAccess.registryAccess().getRegistry(RegistryKey.WORLD_PRESET));
    }

    public static net.minecraft.world.level.levelgen.presets.WorldPreset bukkitToMinecraft(WorldPreset bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> bukkitToMinecraftHolder(WorldPreset bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.WORLD_PRESET);
    }

    private final Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> holder;

    public PaperWorldPreset(final Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> holder) {
        this.holder = holder;
    }

    @Override
    public Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }
}
