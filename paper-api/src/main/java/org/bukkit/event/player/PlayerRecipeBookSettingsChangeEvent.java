package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player changes recipe book settings.
 */
public class PlayerRecipeBookSettingsChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final RecipeBookType recipeBookType;
    private final boolean open;
    private final boolean filtering;

    public PlayerRecipeBookSettingsChangeEvent(@NotNull final Player player, @NotNull final RecipeBookType recipeBookType, final boolean open, final boolean filtering) {
        super(player);
        this.recipeBookType = recipeBookType;
        this.open = open;
        this.filtering = filtering;
    }

    /**
     * Gets the type of recipe book the player is changing the settings for.
     *
     * @return the type of recipe book
     */
    @NotNull
    public RecipeBookType getRecipeBookType() {
        return recipeBookType;
    }

    /**
     * Checks if the recipe book is being opened or closed.
     *
     * @return true if opening
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Checks if the recipe book filter is being enabled or disabled.
     *
     * @return true if enabling
     */
    public boolean isFiltering() {
        return filtering;
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

    /**
     * Enum representing the various types of recipe book.
     * <br>
     * Different types of recipe book are shown in different GUIs.
     */
    public enum RecipeBookType {

        /**
         * Recipe book seen in crafting table and player inventory.
         */
        CRAFTING,
        /**
         * Recipe book seen in furnace.
         */
        FURNACE,
        /**
         * Recipe book seen in blast furnace.
         */
        BLAST_FURNACE,
        /**
         * Recipe book seen in smoker.
         */
        SMOKER;
    }
}
