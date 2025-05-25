package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event that is called when a player right clicks an entity that
 * also contains the location where the entity was clicked.
 * <br>
 * Note that the client may sometimes spuriously send this packet in addition to {@link PlayerInteractEntityEvent}.
 * Users are advised to listen to this (parent) class unless specifically required.
 * <br>
 * Note that interacting with Armor Stands fires this event only and not its parent and as such users are expressly required
 * to listen to this event for that scenario.
 */
public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Vector position;

    @ApiStatus.Internal
    public PlayerInteractAtEntityEvent(@NotNull Player player, @NotNull Entity clickedEntity, @NotNull Vector position) {
        this(player, clickedEntity, position, EquipmentSlot.HAND);
    }

    @ApiStatus.Internal
    public PlayerInteractAtEntityEvent(@NotNull Player player, @NotNull Entity clickedEntity, @NotNull Vector position, @NotNull EquipmentSlot hand) {
        super(player, clickedEntity, hand);
        this.position = position;
    }

    @NotNull
    public Vector getClickedPosition() {
        return this.position.clone();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
