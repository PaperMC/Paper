package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRecipeBookClickEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Recipe;

public class CraftPlayerRecipeBookClickEvent extends CraftPlayerEvent implements PlayerRecipeBookClickEvent {

    private final Recipe originalRecipe;
    private Recipe recipe;
    private boolean shiftClick;

    public CraftPlayerRecipeBookClickEvent(final Player player, final Recipe recipe, final boolean shiftClick) {
        super(player);
        this.originalRecipe = recipe;
        this.recipe = recipe;
        this.shiftClick = shiftClick;
    }

    @Override
    public Recipe getOriginalRecipe() {
        return this.originalRecipe;
    }

    @Override
    public Recipe getRecipe() {
        return this.recipe;
    }

    @Override
    public void setRecipe(final Recipe recipe) {
        Preconditions.checkArgument(recipe != null, "recipe cannot be null");
        if (this.originalRecipe instanceof CraftingRecipe) { // Any type of crafting recipe is acceptable
            Preconditions.checkArgument(recipe instanceof CraftingRecipe, "provided recipe must be a crafting recipe");
        } else { // Other recipes must be the same type
            Preconditions.checkArgument(this.originalRecipe.getClass() == recipe.getClass(), "provided recipe must be of the same type as original recipe");
        }
        this.recipe = recipe;
    }

    @Override
    public boolean isShiftClick() {
        return this.shiftClick;
    }

    @Override
    public void setShiftClick(final boolean shiftClick) {
        this.shiftClick = shiftClick;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerRecipeBookClickEvent.getHandlerList();
    }
}
