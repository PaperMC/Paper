package org.bukkit.event.player;

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.entity.TeleportFlag;
import java.util.Set;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Holds information for player teleport events
 */
public interface PlayerTeleportEvent extends PlayerMoveEvent {

    /**
     * Gets the cause of this teleportation event
     *
     * @return Cause of the event
     */
    TeleportCause getCause();

    /**
     * Returns the relative teleportation flags used in this teleportation.
     * This determines which axis the player will not lose their velocity in.
     *
     * @return an immutable set of relative teleportation flags
     */
    @Unmodifiable Set<TeleportFlag.Relative> getRelativeTeleportationFlags();

    /**
     * Gets if the player will be dismounted in this teleportation.
     *
     * @return dismounted or not
     * @deprecated dismounting on tp is no longer controlled by the server
     */
    @Deprecated(forRemoval = true)
    @Contract("-> true")
    default boolean willDismountPlayer() {
        return true;
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum TeleportCause {
        /**
         * Indicates the teleportation was caused by a player throwing an Ender
         * Pearl
         */
        ENDER_PEARL,
        /**
         * Indicates the teleportation was caused by a player executing a
         * command
         */
        COMMAND,
        /**
         * Indicates the teleportation was caused by a plugin
         */
        PLUGIN,
        /**
         * Indicates the teleportation was caused by a player entering a
         * Nether portal
         */
        NETHER_PORTAL,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * portal
         */
        END_PORTAL,
        /**
         * Indicates the teleportation was caused by a player teleporting to an
         * Entity/Player via the spectator menu
         */
        SPECTATE,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * gateway
         */
        END_GATEWAY,
        /**
         * Indicates the teleportation was caused by a player consuming an item with a {@link ConsumeEffect.TeleportRandomly} effect
         */
        CONSUMABLE_EFFECT,
        /**
         * Indicates the teleportation was caused by a player exiting a vehicle
         */
        DISMOUNT,
        /**
         * Indicates the teleportation was caused by a player exiting a bed
         */
        EXIT_BED,
        /**
         * Indicates the teleportation was caused by an event not covered by
         * this enum
         */
        UNKNOWN;

        /**
         * Indicates the teleportation was caused by a player consuming chorus
         * fruit
         * @deprecated in favor of {@link #CONSUMABLE_EFFECT}
         */
        @Deprecated(since = "1.21.5", forRemoval = true)
        public static final TeleportCause CHORUS_FRUIT = CONSUMABLE_EFFECT;
    }
}
