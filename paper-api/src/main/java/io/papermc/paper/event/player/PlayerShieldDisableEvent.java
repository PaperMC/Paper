package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.checkerframework.checker.index.qual.NonNegative;
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
public interface PlayerShieldDisableEvent extends PlayerEventNew, Cancellable {

    /**
     * Provides the damager that disabled the shield.
     *
     * @return the entity instance that damaged the player in a way that caused the shield to be disabled.
     */
    Entity getDamager();

    /**
     * Gets the cooldown the disabled shield will be disabled for in ticks.
     * <p>
     * Notably, this value is not final as it might be changed by a {@link PlayerItemCooldownEvent} down the line,
     * as said event is called if this event is not cancelled.
     *
     * @return cooldown in ticks
     */
    @NonNegative int getCooldown();

    /**
     * Sets the cooldown of the shield in ticks.
     * <p>
     * Notably, this value is not final as it might be changed by a {@link PlayerItemCooldownEvent} down the line,
     * as said event is called if this event is not cancelled.
     *
     * @param cooldown cooldown in ticks
     */
    void setCooldown(@NonNegative int cooldown);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
