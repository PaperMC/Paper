package org.bukkit.event.player;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player discovers a new recipe in the recipe book.
 */
public class PlayerRecipeDiscoverEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;
    private final NamespacedKey recipe;

    public PlayerRecipeDiscoverEvent(@NotNull Player who, @NotNull NamespacedKey recipe) {
        super(who);
        this.recipe = recipe;
    }

    /**
     * Get the namespaced key of the discovered recipe.
     *
     * @return the discovered recipe
     */
    @NotNull
    public NamespacedKey getRecipe() {
        return recipe;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
