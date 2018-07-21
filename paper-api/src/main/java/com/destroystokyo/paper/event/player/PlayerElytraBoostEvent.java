package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a player boosts elytra flight with a firework
 */
@NullMarked
public class PlayerElytraBoostEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack itemStack;
    private final Firework firework;
    private boolean consume = true;
    private final EquipmentSlot hand;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerElytraBoostEvent(final Player player, final ItemStack itemStack, final Firework firework, final EquipmentSlot hand) {
        super(player);
        this.itemStack = itemStack;
        this.firework = firework;
        this.hand = hand;
    }

    /**
     * Get the firework itemstack used
     *
     * @return ItemStack of firework
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Get the firework entity that was spawned
     *
     * @return Firework entity
     */
    public Firework getFirework() {
        return this.firework;
    }

    /**
     * Get whether to consume the firework or not
     *
     * @return {@code true} to consume
     */
    public boolean shouldConsume() {
        return this.consume;
    }

    /**
     * Set whether to consume the firework or not
     *
     * @param consume {@code true} to consume
     */
    public void setShouldConsume(final boolean consume) {
        this.consume = consume;
    }

    /**
     * Gets the hand holding the firework used for boosting this player.
     *
     * @return interaction hand
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
