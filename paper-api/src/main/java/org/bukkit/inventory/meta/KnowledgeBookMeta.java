package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.NamespacedKey;

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
    List<NamespacedKey> getRecipes();

    /**
     * Clears the existing book recipes, and sets the book to use the provided
     * recipes.
     *
     * @param recipes A list of recipes to set the book to use
     */
    void setRecipes(List<NamespacedKey> recipes);

    /**
     * Adds new recipe to the end of the book.
     *
     * @param recipes A list of recipe keys
     */
    void addRecipe(NamespacedKey... recipes);

    @Override
    KnowledgeBookMeta clone();
}
