package org.bukkit.event.player;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player discovers a new recipe in the recipe book.
 */
public interface PlayerRecipeDiscoverEvent extends PlayerEventNew, Cancellable {

    /**
     * Get the namespaced key of the discovered recipe.
     *
     * @return the discovered recipe
     */
    NamespacedKey getRecipe();

    /**
     * Get if the player should be notified (toast) of the discovery.
     *
     * @return {@code true} if the player should be notified
     */
    boolean shouldShowNotification();

    /**
     * Set if the player should be notified (toast) of the discovery.
     *
     * @param showNotification {@code true} if the player should be notified
     */
    void shouldShowNotification(boolean showNotification);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
