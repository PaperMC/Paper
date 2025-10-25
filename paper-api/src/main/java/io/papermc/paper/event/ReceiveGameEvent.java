package io.papermc.paper.event;

import org.bukkit.GameEvent;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Common methods for {@link io.papermc.paper.event.entity.EntityReceiveGameEvent} and
 * {@link org.bukkit.event.block.BlockReceiveGameEvent}.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface ReceiveGameEvent extends Cancellable {

    /**
     * Get the underlying event.
     *
     * @return the event
     */
    GameEvent getEvent();

    /**
     * Get the entity which triggered this event, if present.
     *
     * @return triggering entity or {@code null}
     */
    @Nullable Entity getTriggerEntity();

    /**
     * Get the block data change which triggered this event, if present.
     *
     * @return triggering block data or {@code null}
     */
    @Nullable BlockData getTriggerBlockData();

    /**
     * {@inheritDoc}
     * <p>
     * Cancels any action the receiver might take because of this game event
     */
    @Override
    void setCancelled(boolean cancel);

    @ApiStatus.Internal
    boolean callEvent();
}
