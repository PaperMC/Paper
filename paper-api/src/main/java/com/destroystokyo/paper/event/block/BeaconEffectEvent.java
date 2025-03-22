package com.destroystokyo.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a beacon effect is being applied to a player.
 */
@NullMarked
public class BeaconEffectEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final boolean primary;
    private PotionEffect effect;

    private boolean cancelled;

    @ApiStatus.Internal
    public BeaconEffectEvent(final Block beacon, final PotionEffect effect, final Player player, final boolean primary) {
        super(beacon);
        this.effect = effect;
        this.player = player;
        this.primary = primary;
    }

    /**
     * Gets the potion effect being applied.
     *
     * @return Potion effect
     */
    public PotionEffect getEffect() {
        return this.effect;
    }

    /**
     * Sets the potion effect that will be applied.
     *
     * @param effect Potion effect
     */
    public void setEffect(final PotionEffect effect) {
        this.effect = effect;
    }

    /**
     * Gets the player who the potion effect is being applied to.
     *
     * @return Affected player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets whether the effect is a primary beacon effect.
     *
     * @return {@code true} if this event represents a primary effect
     */
    public boolean isPrimary() {
        return this.primary;
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
