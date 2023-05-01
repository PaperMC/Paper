package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called whenever a players shield is disabled due to an attack from another entity that was capable of disabling the
 * shield. This, most commonly, may be another player attacking with an axe.
 * <p>
 * Notably, this even is distinct from a {@link PlayerItemCooldownEvent} and will fire prior to the item going on
 * cooldown.
 * It follows that, if this event is cancelled, no {@link PlayerItemCooldownEvent} is called as the shield is never
 * disabled in the first place.
 */
@NullMarked
public class PlayerShieldDisableEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity damager;
    private int cooldown;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerShieldDisableEvent(final Player player, final Entity damager, final int cooldown) {
        super(player);
        this.damager = damager;
        this.cooldown = cooldown;
    }

    /**
     * Provides the damager that disabled the shield.
     *
     * @return the entity instance that damaged the player in a way that caused the shield to be disabled.
     */
    public Entity getDamager() {
        return this.damager;
    }

    /**
     * Gets the cooldown the disabled shield will be disabled for in ticks.
     * <p>
     * Notably, this value is not final as it might be changed by a {@link PlayerItemCooldownEvent} down the line,
     * as said event is called if this event is not cancelled.
     *
     * @return cooldown in ticks
     */
    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * Sets the cooldown of the shield in ticks.
     * <p>
     * Notably, this value is not final as it might be changed by a {@link PlayerItemCooldownEvent} down the line,
     * as said event is called if this event is not cancelled.
     *
     * @param cooldown cooldown in ticks, has to be a positive number
     */
    public void setCooldown(final int cooldown) {
        Preconditions.checkArgument(cooldown >= 0, "The cooldown has to be equal to or greater than 0!");
        this.cooldown = cooldown;
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
