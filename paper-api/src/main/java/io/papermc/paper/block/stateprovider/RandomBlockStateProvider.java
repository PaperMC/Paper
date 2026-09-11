package io.papermc.paper.block.stateprovider;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

@ApiStatus.NonExtendable
public interface RandomBlockStateProvider extends BlockStateProvider {

    /**
     * Candidate block types used by this random provider.
     *
     * @return candidate blocks
     */
    @Contract(pure = true)
    RegistryKeySet<BlockType> blocks();
}
