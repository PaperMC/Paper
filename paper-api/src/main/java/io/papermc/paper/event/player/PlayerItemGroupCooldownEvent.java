package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a player receives an item cooldown.
 *
 * @see PlayerItemCooldownEvent for a more specific event when applied to a specific item.
 */
@NullMarked
public class PlayerItemGroupCooldownEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final NamespacedKey cooldownGroup;
    private int cooldown;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerItemGroupCooldownEvent(final Player player, final NamespacedKey cooldownGroup, final int cooldown) {
        super(player);
        this.cooldownGroup = cooldownGroup;
        this.cooldown = cooldown;
    }

    /**
     * Get the cooldown group as defined by an item's {@link org.bukkit.inventory.meta.components.UseCooldownComponent}.
     *
     * @return cooldown group
     */
    public NamespacedKey getCooldownGroup() {
        return this.cooldownGroup;
    }

    /**
     * Gets the cooldown in ticks.
     *
     * @return cooldown in ticks
     */
    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * Sets the cooldown of the material in ticks.
     * Setting the cooldown to 0 results in removing an already existing cooldown for the material.
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
