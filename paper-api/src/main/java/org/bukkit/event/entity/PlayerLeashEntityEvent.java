package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called immediately prior to a creature being leashed by a player.
 */
public class PlayerLeashEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity leashHolder;
    private final Entity entity;
    private final Player player;
    private final EquipmentSlot hand;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerLeashEntityEvent(@NotNull Entity entity, @NotNull Entity leashHolder, @NotNull Player leasher, @NotNull EquipmentSlot hand) {
        this.leashHolder = leashHolder;
        this.entity = entity;
        this.player = leasher;
        this.hand = hand;
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.2", forRemoval = true)
    public PlayerLeashEntityEvent(@NotNull Entity entity, @NotNull Entity leashHolder, @NotNull Player leasher) {
        this(entity, leashHolder, leasher, EquipmentSlot.HAND);
    }

    /**
     * Returns the entity that is holding the leash.
     *
     * @return The leash holder
     */
    @NotNull
    public Entity getLeashHolder() {
        return this.leashHolder;
    }

    /**
     * Returns the entity being leashed.
     *
     * @return The entity
     */
    @NotNull
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public final Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the hand used by the player to leash the entity.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
