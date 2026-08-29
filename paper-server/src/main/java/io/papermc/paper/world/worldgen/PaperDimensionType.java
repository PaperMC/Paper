package io.papermc.paper.world.worldgen;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperDimensionType extends HolderableBase<net.minecraft.world.level.dimension.DimensionType> implements DimensionType {

    public PaperDimensionType(final Holder<net.minecraft.world.level.dimension.DimensionType> holder) {
        super(holder);
    }

    public static DimensionType minecraftHolderToBukkit(final Holder<net.minecraft.world.level.dimension.DimensionType> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.DIMENSION_TYPE);
    }

    public static Holder<net.minecraft.world.level.dimension.DimensionType> bukkitToMinecraftHolder(final DimensionType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }
}
