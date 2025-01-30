package org.bukkit.inventory;

import org.jspecify.annotations.NullMarked;
import java.util.function.Predicate;

@NullMarked
record PredicateChoiceImpl(Predicate<ItemStack> stackPredicate, ItemStack exampleStack) implements RecipeChoice.PredicateChoice {

    public PredicateChoiceImpl(Predicate<ItemStack> stackPredicate, ItemStack exampleStack) {
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
