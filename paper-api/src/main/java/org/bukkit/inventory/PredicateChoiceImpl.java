package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import org.jspecify.annotations.NullMarked;
import java.util.function.Predicate;

@NullMarked
record PredicateChoiceImpl(Predicate<ItemStack> stackPredicate, ItemStack exampleStack) implements RecipeChoice.PredicateChoice {

    public PredicateChoiceImpl(Predicate<ItemStack> stackPredicate, ItemStack exampleStack) {
        Preconditions.checkArgument(stackPredicate != null, "The item predicate cannot be null");
        Preconditions.checkArgument(exampleStack != null, "The example stack cannot be null");
        Preconditions.checkArgument(!exampleStack.isEmpty(), "Cannot have empty/air example stack");

        this.stackPredicate = stackPredicate;
        this.exampleStack = exampleStack.clone();
    }

    @Override
    public Predicate<ItemStack> getPredicate() {
        return this.stackPredicate;
    }

    @Override
    public ItemStack getItemStack() {
        return this.exampleStack.clone();
    }

    @Override
    public PredicateChoiceImpl clone() {
        return new PredicateChoiceImpl(this.stackPredicate::test, this.exampleStack.clone());
    }

    @Override
    public boolean test(final ItemStack itemStack) {
        return this.stackPredicate.test(itemStack);
    }
}
