package io.papermc.paper.event.player;

import io.papermc.paper.datacomponent.item.UseCooldown;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.checkerframework.checker.index.qual.NonNegative;

/**
 * Fired when a player receives an item cooldown.
 *
 * @see PlayerItemCooldownEvent for a more specific event when applied to a specific item.
 */
public interface PlayerItemGroupCooldownEvent extends PlayerEvent, Cancellable {

    /**
     * Get the cooldown group as defined by an item's {@link UseCooldown#cooldownGroup()}.
     *
     * @return cooldown group
     */
    NamespacedKey getCooldownGroup();

    /**
     * Gets the cooldown in ticks.
     *
     * @return cooldown in ticks
     */
    @NonNegative int getCooldown();

    /**
     * Sets the cooldown of the item in ticks.
     * Setting the cooldown to 0 results in removing an already existing cooldown for the item.
     *
     * @param cooldown cooldown in ticks, has to be a positive number
     */
    void setCooldown(@NonNegative int cooldown);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
