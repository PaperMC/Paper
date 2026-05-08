package io.papermc.paper.world;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperWorldPreset extends HolderableBase<net.minecraft.world.level.levelgen.presets.WorldPreset> implements WorldPreset {

    public PaperWorldPreset(final Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> holder) {
        super(holder);
    }

    public static WorldPreset minecraftToBukkit(final net.minecraft.world.level.levelgen.presets.WorldPreset minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.WORLD_PRESET);
    }

    public static WorldPreset minecraftHolderToBukkit(final Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.WORLD_PRESET);
    }

    public static net.minecraft.world.level.levelgen.presets.WorldPreset bukkitToMinecraft(final WorldPreset bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.level.levelgen.presets.WorldPreset> bukkitToMinecraftHolder(final WorldPreset bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.WORLD_PRESET);
    }
}
