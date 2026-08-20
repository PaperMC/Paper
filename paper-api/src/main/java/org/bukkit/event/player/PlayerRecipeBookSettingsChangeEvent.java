package org.bukkit.event.player;

import org.bukkit.event.HandlerList;

/**
 * Called when a player changes recipe book settings.
 */
public interface PlayerRecipeBookSettingsChangeEvent extends PlayerEvent {

    /**
     * Gets the type of recipe book the player is changing the settings for.
     *
     * @return the type of recipe book
     */
    RecipeBookType getRecipeBookType();

    /**
     * Checks if the recipe book is being opened or closed.
     *
     * @return {@code true} if opening
     */
    boolean isOpen();

    /**
     * Checks if the recipe book filter is being enabled or disabled.
     *
     * @return {@code true} if enabling
     */
    boolean isFiltering();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Enum representing the various types of recipe book.
     * <br>
     * Different types of recipe book are shown in different GUIs.
     */
    enum RecipeBookType {

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
        SMOKER
    }
}
