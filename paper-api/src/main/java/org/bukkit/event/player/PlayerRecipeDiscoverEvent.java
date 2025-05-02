package org.bukkit.event.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player discovers a new recipe in the recipe book.
 */
public class PlayerRecipeDiscoverEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final NamespacedKey recipe;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerRecipeDiscoverEvent(@NotNull Player player, @NotNull NamespacedKey recipe) {
        super(player);
        this.recipe = recipe;
    }

    /**
     * Get the namespaced key of the discovered recipe.
     *
     * @return the discovered recipe
     */
    @NotNull
    public NamespacedKey getRecipe() {
        return this.recipe;
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
