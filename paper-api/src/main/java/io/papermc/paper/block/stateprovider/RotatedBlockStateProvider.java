package io.papermc.paper.block.stateprovider;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface RotatedBlockStateProvider extends BlockStateProvider {

    /**
     * Source provider whose output is rotated.
     *
     * @return source provider
     */
    BlockStateProvider stateProvider();

    /**
     * Forced direction for rotation, or null to pick one at random.
     *
     * @return forced direction, or null
     */
    @Nullable BlockFace direction();
}
