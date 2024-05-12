package org.bukkit.inventory;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
record EmptyRecipeChoice() implements RecipeChoice {

    static final RecipeChoice INSTANCE = new EmptyRecipeChoice();
    @Override
    public ItemStack getItemStack() {
        throw new UnsupportedOperationException("This is an empty RecipeChoice");
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public RecipeChoice clone() {
        return this;
    }

    @Override
    public boolean test(final ItemStack itemStack) {
        return false;
    }

    @Override
    public RecipeChoice validate(final boolean allowEmptyRecipes) {
        if (allowEmptyRecipes) return this;
        throw new IllegalArgumentException("empty RecipeChoice isn't allowed here");
    }
}
