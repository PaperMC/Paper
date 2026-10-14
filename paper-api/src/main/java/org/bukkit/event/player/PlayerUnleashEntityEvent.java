package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called prior to an entity being unleashed due to a player's action.
 */
public class PlayerUnleashEntityEvent extends EntityUnleashEvent implements Cancellable {

    private final Player player;
    private final EquipmentSlot hand;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerUnleashEntityEvent(@NotNull Entity entity, @NotNull Player player, @NotNull EquipmentSlot hand, boolean dropLeash) {
        super(entity, UnleashReason.PLAYER_UNLEASH, dropLeash);
        this.player = player;
        this.hand = hand;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerUnleashEntityEvent(@NotNull Entity entity, @NotNull Player player, @NotNull EquipmentSlot hand) {
        this(entity, player, hand, false);
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.2", forRemoval = true)
    public PlayerUnleashEntityEvent(@NotNull Entity entity, @NotNull Player player) {
        this(entity, player, EquipmentSlot.HAND);
    }

    /**
     * Returns the player who is unleashing the entity.
     *
     * @return The player
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the hand used by the player to unleash the entity.
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
}
