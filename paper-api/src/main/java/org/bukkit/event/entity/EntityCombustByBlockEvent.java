package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a block causes an entity to combust.
 */
public class EntityCombustByBlockEvent extends EntityCombustEvent {

    private final Block combuster;

    @ApiStatus.Internal
    @Deprecated(since = "1.21", forRemoval = true)
    public EntityCombustByBlockEvent(@Nullable final Block combuster, @NotNull final Entity combustee, final int duration) {
        this(combuster, combustee, (float) duration);
    }

    @ApiStatus.Internal
    public EntityCombustByBlockEvent(@Nullable final Block combuster, @NotNull final Entity combustee, final float duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    /**
     * The combuster can be lava or a block that is on fire.
     * <p>
     * WARNING: block may be {@code null}.
     *
     * @return the Block that set the combustee alight.
     */
    @Nullable
    public Block getCombuster() {
        return this.combuster;
    }
}
