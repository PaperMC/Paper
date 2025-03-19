package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Event, that is fired when it is decided whether Player attack will be a critical hit or not.
 * It will be fired cancelled when the hit will not be a critical hit. When cancelled, the hit won't be critical.
 */
@NullMarked
public class PlayerCriticalHitEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity attacked;
    private float multiplier;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerCriticalHitEntityEvent(final Player who, final Entity attacked, final float multiplier, final boolean critical) {
        super(who);
        this.attacked = attacked;
        this.multiplier = multiplier;
        this.cancelled = !critical;
    }

    /**
     * Gets the entity that was attacked in this event.
     *
     * @return entity that was attacked
     */
    public Entity getAttacked() {
        return attacked;
    }

    /**
     * Gets a multiplier by which the damage will be multiplied when the hit is critical.
     *
     * @return critical hit damage multiplier
     */
    public float getMultiplier() {
        return multiplier;
    }

    /**
     * Sets a multiplier by which the damage will be multiplied if the hit is critical
     *
     * @param multiplier critical hit damage multiplier
     */
    public void setMultiplier(final float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
