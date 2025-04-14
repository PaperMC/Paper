package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event that is called when a player right clicks an entity.
 */
public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected Entity clickedEntity;
    private final EquipmentSlot hand;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerInteractEntityEvent(@NotNull final Player player, @NotNull final Entity clickedEntity) {
        this(player, clickedEntity, EquipmentSlot.HAND);
    }

    @ApiStatus.Internal
    public PlayerInteractEntityEvent(@NotNull final Player player, @NotNull final Entity clickedEntity, @NotNull final EquipmentSlot hand) {
        super(player);
        this.clickedEntity = clickedEntity;
        this.hand = hand;
    }

    /**
     * Gets the entity that was right-clicked by the player.
     *
     * @return entity right clicked by player
     */
    @NotNull
    public Entity getRightClicked() {
        return this.clickedEntity;
    }

    /**
     * The hand used to perform this interaction.
     *
     * @return the hand used to interact
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
