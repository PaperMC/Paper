package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a block causes an entity to combust.
 *
 * @since 1.1.0 R1
 */
public class EntityCombustByBlockEvent extends EntityCombustEvent {
    private final Block combuster;

    @Deprecated(since = "1.21")
    public EntityCombustByBlockEvent(@Nullable final Block combuster, @NotNull final Entity combustee, final int duration) {
        this(combuster, combustee, (float) duration);
    }

    public EntityCombustByBlockEvent(@Nullable final Block combuster, @NotNull final Entity combustee, final float duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    /**
     * The combuster can be lava or a block that is on fire.
     * <p>
     * WARNING: block may be null.
     *
     * @return the Block that set the combustee alight.
     */
    @Nullable
    public Block getCombuster() {
        return combuster;
    }
}
