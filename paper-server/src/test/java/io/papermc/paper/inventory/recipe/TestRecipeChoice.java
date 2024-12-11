package io.papermc.paper.inventory.recipe;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
class TestRecipeChoice {

    @Test
    void testRecipeChoices() {
        final Iterator<Recipe> iter = Bukkit.recipeIterator();
        boolean foundRecipes = false;
        while (iter.hasNext()) {
            foundRecipes = true;
            assertDoesNotThrow(iter::next, "Failed to convert a recipe to Bukkit recipe!");
        }
        assertTrue(foundRecipes, "No recipes found!");
    }
}
