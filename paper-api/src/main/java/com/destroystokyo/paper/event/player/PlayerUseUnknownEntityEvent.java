package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents an event that is called when a player clicks an unknown entity.
 * Useful for plugins dealing with virtual entities (entities that aren't actually spawned on the server).
 * <br>
 * This event may be called multiple times per interaction with different interaction hands
 * and with or without the clicked position.
 */
@NullMarked
public class PlayerUseUnknownEntityEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int entityId;
    private final boolean attack;
    private final EquipmentSlot hand;
    private final @Nullable Vector clickedPosition;

    @ApiStatus.Internal
    public PlayerUseUnknownEntityEvent(final Player player, final int entityId, final boolean attack, final EquipmentSlot hand, final @Nullable Vector clickedPosition) {
        super(player);
        this.entityId = entityId;
        this.attack = attack;
        this.hand = hand;
        this.clickedPosition = clickedPosition;
    }

    /**
     * Returns the entity id of the unknown entity that was interacted with.
     *
     * @return the entity id of the entity that was interacted with
     */
    public int getEntityId() {
        return this.entityId;
    }

    /**
     * Returns whether the interaction was an attack.
     *
     * @return {@code true} if the player is attacking the entity, {@code false} if the player is interacting with the entity
     */
    public boolean isAttack() {
        return this.attack;
    }

    /**
     * Returns the hand used to perform this interaction.
     *
     * @return the hand used to interact
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Returns the position relative to the entity that was clicked, or {@code null} if not available.
     * See {@link PlayerInteractAtEntityEvent} for more details.
     *
     * @return the position relative to the entity that was clicked, or {@code null} if not available
     * @see PlayerInteractAtEntityEvent
     */
    public @Nullable Vector getClickedRelativePosition() {
        return this.clickedPosition != null ? this.clickedPosition.clone() : null;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
