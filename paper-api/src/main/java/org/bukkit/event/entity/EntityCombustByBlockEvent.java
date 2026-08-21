package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block causes an entity to combust.
 */
public interface EntityCombustByBlockEvent extends EntityCombustEvent {

    /**
     * The combuster can be lava or a block that is on fire.
     * <p>
     * WARNING: block may be {@code null}.
     *
     * @return the Block that set the combustee alight.
     */
    @Nullable Block getCombuster();
}
