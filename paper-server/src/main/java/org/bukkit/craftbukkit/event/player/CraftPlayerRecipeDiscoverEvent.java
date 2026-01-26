package org.bukkit.craftbukkit.event.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;

public class CraftPlayerRecipeDiscoverEvent extends CraftPlayerEvent implements PlayerRecipeDiscoverEvent {

    private final NamespacedKey recipe;
    private boolean cancelled;
    private boolean showNotification;

    public CraftPlayerRecipeDiscoverEvent(final Player player, final NamespacedKey recipe, final boolean showNotification) {
        super(player);
        this.recipe = recipe;
        this.showNotification = showNotification;
    }

    @Override
    public NamespacedKey getRecipe() {
        return this.recipe;
    }

    @Override
    public boolean shouldShowNotification() {
        return this.showNotification;
    }

    @Override
    public void shouldShowNotification(final boolean showNotification) {
        this.showNotification = showNotification;
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
        return PlayerRecipeDiscoverEvent.getHandlerList();
    }
}
