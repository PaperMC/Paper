package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block is ignited. If you want to catch when a Player places
 * fire, you need to use {@link BlockPlaceEvent}.
 * <p>
 * If this event is cancelled, the block will not be ignited.
 */
public interface BlockIgniteEvent extends BlockEvent, Cancellable {

    /**
     * Gets the cause of block ignite.
     *
     * @return An IgniteCause value detailing the cause of block ignition
     */
    IgniteCause getCause();

    /**
     * Gets the player who ignited this block
     *
     * @return The Player that placed/ignited the fire block, or {@code null} if not ignited by a Player.
     */
    @Nullable Player getPlayer();

    /**
     * Gets the entity who ignited this block
     *
     * @return The Entity that placed/ignited the fire block, or {@code null} if not ignited by an Entity.
     */
    @Nullable Entity getIgnitingEntity();

    /**
     * Gets the block which ignited this block
     *
     * @return The Block that placed/ignited the fire block, or {@code null} if not ignited by a Block.
     */
    @Nullable Block getIgnitingBlock();

    /**
     * An enum to specify the cause of the ignite
     */
    enum IgniteCause {

        /**
         * Block ignition caused by lava.
         */
        LAVA,
        /**
         * Block ignition caused by a player or dispenser using flint-and-steel.
         */
        FLINT_AND_STEEL,
        /**
         * Block ignition caused by dynamic spreading of fire.
         */
        SPREAD,
        /**
         * Block ignition caused by lightning.
         */
        LIGHTNING,
        /**
         * Block ignition caused by an entity using a fireball.
         */
        FIREBALL,
        /**
         * Block ignition caused by an Ender Crystal.
         */
        ENDER_CRYSTAL,
        /**
         * Block ignition caused by explosion.
         */
        EXPLOSION,
        /**
         * Block ignition caused by a flaming arrow.
         */
        ARROW
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
