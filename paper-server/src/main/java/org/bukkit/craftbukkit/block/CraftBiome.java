package org.bukkit.craftbukkit.block;

import io.papermc.paper.util.OldEnumHolderable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftBiome extends OldEnumHolderable<Biome, net.minecraft.world.level.biome.Biome> implements Biome {

    private static int count = 0;

    public static Biome minecraftToBukkit(net.minecraft.world.level.biome.Biome minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.BIOME);
    }

    public static Biome minecraftHolderToBukkit(Holder<net.minecraft.world.level.biome.Biome> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.BIOME);
    }

    public static net.minecraft.world.level.biome.@Nullable Biome bukkitToMinecraft(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }

        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static @Nullable Holder<net.minecraft.world.level.biome.Biome> bukkitToMinecraftHolder(Biome bukkit) {
        if (bukkit == Biome.CUSTOM) {
            return null;
        }
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.BIOME);
    }

    public CraftBiome(final Holder<net.minecraft.world.level.biome.Biome> holder) {
        super(holder, count++);
    }
}
