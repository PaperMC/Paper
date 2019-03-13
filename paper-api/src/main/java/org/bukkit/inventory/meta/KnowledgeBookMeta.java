package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface KnowledgeBookMeta extends ItemMeta {

    /**
     * Checks for the existence of recipes in the book.
     *
     * @return true if the book has recipes
     */
    boolean hasRecipes();

    /**
     * Gets all the recipes in the book.
     *
     * @return list of all the recipes in the book
     */
    @NotNull
    List<NamespacedKey> getRecipes();

    /**
     * Clears the existing book recipes, and sets the book to use the provided
     * recipes.
     *
     * @param recipes A list of recipes to set the book to use
     */
    void setRecipes(@NotNull List<NamespacedKey> recipes);

    /**
     * Adds new recipe to the end of the book.
     *
     * @param recipes A list of recipe keys
     */
    void addRecipe(@NotNull NamespacedKey... recipes);

    @NotNull
    @Override
    KnowledgeBookMeta clone();
}
