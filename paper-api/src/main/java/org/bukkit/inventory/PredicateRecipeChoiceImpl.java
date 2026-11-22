package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import com.google.common.collect.Lists;
import org.jspecify.annotations.NullMarked;

@NullMarked
record PredicateRecipeChoiceImpl(Predicate<? super ItemStack> stackPredicate, List<ItemStack> exampleStacks) implements RecipeChoice.PredicateChoice {

    public PredicateRecipeChoiceImpl {
        Preconditions.checkArgument(stackPredicate != null, "The item predicate cannot be null");
        Preconditions.checkArgument(!exampleStacks.isEmpty(), "The example stacks cannot be empty");
        Preconditions.checkArgument(exampleStacks.stream().noneMatch(Objects::isNull), "Cannot have null example stack");
        Preconditions.checkArgument(exampleStacks.stream().noneMatch(ItemStack::isEmpty), "Cannot have empty/air example stack");

        exampleStacks = new ArrayList<>(exampleStacks.stream().map(ItemStack::clone).toList());
    }

    @Override
    public ItemStack getItemStack() {
        return this.exampleStacks.get(0).clone();
    }

    public List<ItemStack> getExampleStacks() {
        return Collections.unmodifiableList(this.exampleStacks);
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "FunctionalExpressionCanBeFolded"})
    @Override
    public PredicateRecipeChoiceImpl clone() {
        return new PredicateRecipeChoiceImpl(this.stackPredicate::test, this.exampleStacks);
    }

    @Override
    public boolean test(final ItemStack itemStack) {
        return this.stackPredicate.test(itemStack);
    }

    @Override
    public RecipeChoice validate(final boolean allowEmptyRecipes) {
        return this;
    }
}
