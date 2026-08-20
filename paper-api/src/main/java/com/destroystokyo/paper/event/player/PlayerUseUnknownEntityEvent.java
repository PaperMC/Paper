package com.destroystokyo.paper.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Represents an event that is called when a player clicks an unknown entity.
 * Useful for plugins dealing with virtual entities (entities that aren't actually spawned on the server).
 * <br>
 * This event may be called multiple times per interaction with different interaction hands
 * and with or without the clicked position.
 */
public interface PlayerUseUnknownEntityEvent extends PlayerEvent {

    /**
     * Returns the entity id of the unknown entity that was interacted with.
     *
     * @return the entity id of the entity that was interacted with
     */
    int getEntityId();

    /**
     * Returns whether the interaction was an attack.
     *
     * @return {@code true} if the player is attacking the entity, {@code false} if the player is interacting with the entity
     */
    boolean isAttack();

    /**
     * Returns the hand used to perform this interaction.
     *
     * @return the hand used to interact
     */
    EquipmentSlot getHand();

    /**
     * Returns the position relative to the entity that was clicked, or {@code null} if not available.
     * See {@link PlayerInteractAtEntityEvent} for more details.
     *
     * @return the position relative to the entity that was clicked, or {@code null} if not available
     * @see PlayerInteractAtEntityEvent
     */
    @Nullable Vector getClickedRelativePosition();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
