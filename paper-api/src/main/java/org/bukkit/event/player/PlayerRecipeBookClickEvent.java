package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Recipe;

/**
 * Called when a player clicks a recipe in the recipe book.
 *
 * @deprecated use {@link com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent}
 */
@Warning
@Deprecated(forRemoval = true)
public interface PlayerRecipeBookClickEvent extends PlayerEvent {

    /**
     * Gets the original recipe the player was trying to craft.
     * <br>
     * This <em>will not</em> reflect any changes made with {@link #setRecipe(Recipe)}.
     *
     * @return the original recipe
     */
    Recipe getOriginalRecipe();

    /**
     * Gets the recipe the player is trying to craft.
     * <br>
     * This <em>will</em> reflect changes made with {@link #setRecipe(Recipe)}.
     *
     * @return the recipe
     */
    Recipe getRecipe();

    /**
     * Set the recipe that will be used.
     * <br>
     * The game will attempt to move the ingredients for this recipe into the
     * appropriate slots.
     * <p>
     * If the original recipe is a {@link CraftingRecipe} the provided recipe
     * must also be a {@link CraftingRecipe}, otherwise the provided recipe must
     * be of the same type as the original recipe.
     *
     * @param recipe the recipe to be used
     */
    void setRecipe(Recipe recipe);

    /**
     * If {@code true} the game will attempt to move the ingredients for as many copies
     * of this recipe as possible into the appropriate slots, otherwise only 1
     * copy will be moved.
     *
     * @return whether as many copies as possible should be moved
     */
    boolean isShiftClick();

    /**
     * Sets if the game will attempt to move the ingredients for as many copies
     * of this recipe as possible into the appropriate slots.
     *
     * @param shiftClick whether as many copies as possible should be moved
     */
    void setShiftClick(boolean shiftClick);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
