package com.destroystokyo.paper.event.player;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player clicks a recipe in the recipe book
 */
@NullMarked
public interface PlayerRecipeBookClickEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the namespaced key of the recipe that was clicked by the player
     *
     * @return The namespaced key of the recipe
     */
    NamespacedKey getRecipe();

    /**
     * Changes what recipe is requested. This sets the requested recipe to the recipe with the given key
     *
     * @param recipe The key of the recipe that should be requested
     */
    void setRecipe(NamespacedKey recipe);

    /**
     * Gets a boolean which indicates whether the player requested to make the maximum amount of results. This is
     * {@code true} if shift is pressed while the recipe is clicked in the recipe book
     *
     * @return {@code true} if shift is pressed while the recipe is clicked
     */
    boolean isMakeAll();

    /**
     * Sets whether the maximum amount of results should be made. If this is {@code true}, the request is handled as if
     * the player had pressed shift while clicking on the recipe
     *
     * @param makeAll {@code true} if the request should attempt to make the maximum amount of results
     */
    void setMakeAll(boolean makeAll);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
