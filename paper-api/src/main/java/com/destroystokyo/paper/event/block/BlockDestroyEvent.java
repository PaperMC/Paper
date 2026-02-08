package com.destroystokyo.paper.event.block;

import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Fired anytime the server intends to 'destroy' a block through some triggering reason.
 * This does not fire anytime a block is set to air, but only with more direct triggers such
 * as physics updates, pistons, entities changing blocks, commands set to "Destroy".
 * <p>
 * This event is associated with the game playing a sound effect at the block in question, when
 * something can be described as "intend to destroy what is there",
 * <p>
 * Events such as leaves decaying, pistons retracting (where the block is moving), does NOT fire this event.
 */
@NullMarked
public interface BlockDestroyEvent extends BlockExpEvent, Cancellable {

    /**
     * Gets the effect that will be played when the block is broken.
     *
     * @return block break effect
     */
    BlockData getEffectBlock();

    /**
     * Sets the effect that will be played when the block is broken.
     * Note: {@link #playEffect()} must be {@code true} in order for this effect to be
     * played.
     *
     * @param effectBlock block effect
     */
    void setEffectBlock(BlockData effectBlock);

    /**
     * @return The new state of this block (Air, or a Fluid type)
     */
    BlockData getNewState();

    /**
     * @return If the server is going to drop the block in question with this destroy event
     */
    boolean willDrop();

    /**
     * @param willDrop If the server is going to drop the block in question with this destroy event
     */
    void setWillDrop(boolean willDrop);

    /**
     * @return If the server is going to play the sound effect for this destruction
     */
    boolean playEffect();

    /**
     * @param playEffect If the server should play the sound effect for this destruction
     */
    void setPlayEffect(boolean playEffect);

    /**
     * @return If the event is cancelled, meaning the block will not be destroyed
     */
    @Override
    boolean isCancelled();

    /**
     * If the event is cancelled, the block will remain in its previous state.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
