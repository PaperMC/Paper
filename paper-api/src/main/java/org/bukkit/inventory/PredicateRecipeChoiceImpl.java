package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import org.jspecify.annotations.NullMarked;
import java.util.function.Predicate;

@NullMarked
record PredicateRecipeChoiceImpl(Predicate<ItemStack> stackPredicate, ItemStack exampleStack) implements RecipeChoice.PredicateChoice {

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

    @Override
    public PredicateRecipeChoiceImpl clone() {
        return new PredicateRecipeChoiceImpl(this.stackPredicate::test, this.exampleStack.clone());
    }

    @Override
    public boolean test(final ItemStack itemStack) {
        return this.stackPredicate.test(itemStack);
    }
}
