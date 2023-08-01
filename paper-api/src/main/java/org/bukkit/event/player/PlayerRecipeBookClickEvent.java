package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player clicks a recipe in the recipe book.
 */
public class PlayerRecipeBookClickEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Recipe originalRecipe;
    private Recipe recipe;
    private boolean shiftClick;

    public PlayerRecipeBookClickEvent(@NotNull final Player player, @NotNull final Recipe recipe, boolean shiftClick) {
        super(player);
        this.originalRecipe = recipe;
        this.recipe = recipe;
        this.shiftClick = shiftClick;
    }

    /**
     * Gets the original recipe the player was trying to craft. <br>
     * This <em>will not</em> reflect any changes made with {@link setRecipe}.
     *
     * @return the original recipe
     */
    @NotNull
    public Recipe getOriginalRecipe() {
        return this.originalRecipe;
    }

    /**
     * Gets the recipe the player is trying to craft. <br>
     * This <em>will</em> reflect changes made with {@link setRecipe}.
     *
     * @return the recipe
     */
    @NotNull
    public Recipe getRecipe() {
        return this.recipe;
    }

    /**
     * Set the recipe that will be used. <br>
     * The game will attempt to move the ingredients for this recipe into the
     * appropriate slots.
     * <p>
     * If the original recipe is a {@link CraftingRecipe} the provided recipe
     * must also be a {@link CraftingRecipe}, otherwise the provided recipe must
     * be of the same type as the original recipe.
     *
     * @param recipe the recipe to be used
     */
    public void setRecipe(@NotNull Recipe recipe) {
        Preconditions.checkArgument(recipe != null, "recipe cannot be null");
        if (this.originalRecipe instanceof CraftingRecipe) { // Any type of crafting recipe is acceptable
            Preconditions.checkArgument(recipe instanceof CraftingRecipe, "provided recipe must be a crafting recipe");
        } else { // Other recipes must be the same type
            Preconditions.checkArgument(this.originalRecipe.getClass() == recipe.getClass(), "provided recipe must be of the same type as original recipe");
        }
        this.recipe = recipe;
    }

    /**
     * If true the game will attempt to move the ingredients for as many copies
     * of this recipe as possible into the appropriate slots, otherwise only 1
     * copy will be moved.
     *
     * @return whether as many copies as possible should be moved
     */
    public boolean isShiftClick() {
        return this.shiftClick;
    }

    /**
     * Sets if the game will attempt to move the ingredients for as many copies
     * of this recipe as possible into the appropriate slots.
     *
     * @param shiftClick whether as many copies as possible should be moved
     */
    public void setShiftClick(boolean shiftClick) {
        this.shiftClick = shiftClick;
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
