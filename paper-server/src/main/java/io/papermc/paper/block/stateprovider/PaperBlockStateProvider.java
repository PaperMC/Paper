package io.papermc.paper.block.stateprovider;

import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import org.bukkit.craftbukkit.block.CraftBlockType;

public final class PaperBlockStateProvider {

    private PaperBlockStateProvider() {
    }

    public static net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider toVanilla(final BlockStateProvider provider) {
        if (provider instanceof final SimpleBlockStateProvider simple) {
            return new SimpleStateProvider(CraftBlockType.bukkitToMinecraftNew(simple.blockType()).defaultBlockState());
        }
        if (provider instanceof final CopyPropertiesBlockStateProvider copyProperties) {
            return new net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider(
                CraftBlockType.bukkitToMinecraftNew(copyProperties.blockType())
            );
        }
        throw new UnsupportedOperationException("Unsupported block state provider type: " + provider.getClass().getSimpleName());
    }
}
