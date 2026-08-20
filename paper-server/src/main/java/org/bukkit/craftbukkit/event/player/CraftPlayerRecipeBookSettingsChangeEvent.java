package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRecipeBookSettingsChangeEvent;

public class CraftPlayerRecipeBookSettingsChangeEvent extends CraftPlayerEvent implements PlayerRecipeBookSettingsChangeEvent {

    private final RecipeBookType recipeBookType;
    private final boolean open;
    private final boolean filtering;

    public CraftPlayerRecipeBookSettingsChangeEvent(final Player player, final RecipeBookType recipeBookType, final boolean open, final boolean filtering) {
        super(player);
        this.recipeBookType = recipeBookType;
        this.open = open;
        this.filtering = filtering;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public boolean isFiltering() {
        return this.filtering;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerRecipeBookSettingsChangeEvent.getHandlerList();
    }
}
