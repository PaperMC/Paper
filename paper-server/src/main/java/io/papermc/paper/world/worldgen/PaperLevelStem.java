package io.papermc.paper.world.worldgen;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperLevelStem extends HolderableBase<net.minecraft.world.level.dimension.LevelStem> implements LevelStem {

    public PaperLevelStem(final Holder<net.minecraft.world.level.dimension.LevelStem> holder) {
        super(holder);
    }

    public static LevelStem minecraftToBukkit(final net.minecraft.world.level.dimension.LevelStem minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.LEVEL_STEM);
    }

    public static LevelStem minecraftHolderToBukkit(final Holder<net.minecraft.world.level.dimension.LevelStem> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.LEVEL_STEM);
    }

    public static net.minecraft.world.level.dimension.LevelStem bukkitToMinecraft(final LevelStem bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.level.dimension.LevelStem> bukkitToMinecraftHolder(final LevelStem bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.LEVEL_STEM);
    }

    @Override
    public DimensionType getDimensionType() {
        return PaperDimensionType.minecraftHolderToBukkit(this.getHandle().type());
    }
}
