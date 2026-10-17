package io.papermc.paper.datacomponent.item.blocktransformer;

import java.util.List;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.Unmodifiable;

public final class PaperBlockTransformer extends HolderableBase<net.minecraft.core.component.BlockTransformer> implements BlockTransformer {

    public static BlockTransformer minecraftHolderToBukkit(final Holder<net.minecraft.core.component.BlockTransformer> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.BLOCK_TRANSFORMER);
    }

    public static Holder<net.minecraft.core.component.BlockTransformer> bukkitToMinecraftHolder(final BlockTransformer bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public PaperBlockTransformer(final Holder<net.minecraft.core.component.BlockTransformer> holder) {
        super(holder);
    }

    @Override
    public @Unmodifiable List<BlockTransformData> transforms() {
        return this.getHandle().transforms().stream().map(PaperBlockTransformData::new).map(BlockTransformData.class::cast).toList();
    }

    public static Holder<net.minecraft.core.component.BlockTransformer> abukkitToMinecraftHolder(final BlockTransformer blockTransformer) {
        if (!(blockTransformer instanceof final PaperBlockTransformer paper)) {
            throw new IllegalArgumentException("Custom block_transformer values must be registered in minecraft:block_transformer before syncing to clients");
        }
        if (paper.getHolder().kind() != Holder.Kind.REFERENCE) {
            throw new IllegalArgumentException("Custom block_transformer values must be registered in minecraft:block_transformer before syncing to clients");
        }
        return paper.getHolder();
    }
}
