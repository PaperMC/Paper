package io.papermc.paper.block.stateprovider;

import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public final class PaperBlockStateProvider {

    private PaperBlockStateProvider() {
    }

    public static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toVanilla(
        final BlockStateProvider provider
    ) {
        final BlockData simple = provider.simple();
        if (simple == null) {
            throw new UnsupportedOperationException("Unsupported block state provider type");
        }
        if (!(simple instanceof final CraftBlockData craftBlockData)) {
            throw new IllegalArgumentException("Unsupported BlockData implementation: " + simple.getClass().getName());
        }
        return new SimpleStateProvider(craftBlockData.getState());
    }

}
