package io.papermc.paper.block.stateprovider;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface RandomBlockStateProvider extends BlockStateProvider {

    /**
     * Candidate block types used by this random provider.
     *
     * @return candidate blocks
     */
    RegistryKeySet<BlockType> blocks();
}
