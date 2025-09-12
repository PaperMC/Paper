package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;

@NullMarked
record PredicateRecipeChoiceImpl(Predicate<? super ItemStack> stackPredicate, ItemStack exampleStack) implements RecipeChoice.PredicateChoice {

    public PredicateRecipeChoiceImpl {
        Preconditions.checkArgument(stackPredicate != null, "The item predicate cannot be null");
        Preconditions.checkArgument(exampleStack != null, "The example stack cannot be null");
        Preconditions.checkArgument(!exampleStack.isEmpty(), "Cannot have empty/air example stack");

        exampleStack = exampleStack.clone();
    }

    @Override
    public ItemStack getItemStack() {
        return this.exampleStack.clone();
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "FunctionalExpressionCanBeFolded"})
    @Override
    public PredicateRecipeChoiceImpl clone() {
        return new PredicateRecipeChoiceImpl(this.stackPredicate::test, this.exampleStack.clone());
    }

    @Override
    public boolean test(final ItemStack itemStack) {
        return this.stackPredicate.test(itemStack);
    }

    @Override
    public RecipeChoice validate(final boolean allowEmptyRecipes) {
        if (this.exampleStack.getType().isAir()) {
            throw new IllegalArgumentException("RecipeChoice.ExactChoice cannot contain air");
        }
        return this;
    }
}
