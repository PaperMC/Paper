package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a potential item match within a recipe. All choices within a
 * recipe must be satisfied for it to be craftable. Choices must never be
 * null or air.
 *
 * <b>This class is not legal for implementation by plugins!</b>
 */
public interface RecipeChoice extends Predicate<ItemStack>, Cloneable {

    // Paper start - add "empty" choice

    /**
     * An "empty" recipe choice. Only valid as a recipe choice in
     * specific places. Check the javadocs of a method before using it
     * to be sure it's valid for that recipe and ingredient type.
     *
     * @return the empty recipe choice
     */
    static @NotNull RecipeChoice empty() {
        return EmptyRecipeChoice.INSTANCE;
    }
    // Paper end

    /**
     * Creates a choice that will be valid only if one of the stacks is
     * exactly matched (aside from stack size).
     *
     * @param first  an ItemStack to match against.
     *               Cannot be null or empty/air.
     * @param others additional ItemStacks to match against.
     * @return a new ExactChoice
     */
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull ExactChoice exactChoice(@NotNull ItemStack first, @NotNull ItemStack... others) {
        List<ItemStack> stacks = new ArrayList<>(others.length + 1);
        stacks.add(first);
        Collections.addAll(stacks, others);
        return new ExactChoice(stacks);
    }

    /**
     * Creates a choice that will be valid only if one of the stacks is
     * exactly matched (aside from stack size).
     *
     * @param stacks the ItemStacks to match against.
     *               Cannot be empty or contain empty/air stacks.
     * @return a new ExactChoice
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull ExactChoice exactChoice(@NotNull List<ItemStack> stacks) {
        return new ExactChoice(stacks);
    }

    /**
     * Creates a recipe choice that will be valid only if an item matches the
     * given predicate.
     * <p>
     * <b>Note:</b> Mutating the {@link ItemStack} within the predicate is not
     * supported.
     *
     * @param stackPredicate the predicate to match against.
     * @param exampleStack   an example {@link ItemStack} to be shown in the
     *                       recipe book. Cannot be empty or air.
     * @return a new PredicateChoice
     */
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull PredicateChoice predicateChoice(@NotNull Predicate<? super ItemStack> stackPredicate, @NotNull ItemStack exampleStack) {
        return new PredicateRecipeChoiceImpl(stackPredicate, exampleStack);
    }

    /**
     * Gets a single item stack representative of this stack choice.
     *
     * @return a single representative item
     * @deprecated for compatibility only
     */
    @Deprecated(since = "1.13.1")
    @NotNull
    ItemStack getItemStack();

    @NotNull
    RecipeChoice clone();

    @Override
    boolean test(@NotNull ItemStack itemStack);

    // Paper start - check valid ingredients
    @org.jetbrains.annotations.ApiStatus.Internal
    default @NotNull RecipeChoice validate(final boolean allowEmptyRecipes) {
        return this;
    }
    // Paper end - check valid ingredients

    /**
     * Represents a choice of multiple matching Materials.
     */
    public static class MaterialChoice implements RecipeChoice {

        private List<Material> choices;

        public MaterialChoice(@NotNull Material choice) {
            this(Arrays.asList(choice));
        }

        public MaterialChoice(@NotNull Material... choices) {
            this(Arrays.asList(choices));
        }

        /**
         * Constructs a MaterialChoice with the current values of the specified
         * tag.
         *
         * @param choices the tag
         */
        public MaterialChoice(@NotNull Tag<Material> choices) {
            this(new ArrayList<>(java.util.Objects.requireNonNull(choices, "Cannot create a material choice with null tag").getValues())); // Paper - delegate to list ctor to make sure all checks are called
        }

        public MaterialChoice(@NotNull List<Material> choices) {
            Preconditions.checkArgument(choices != null, "choices");
            Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");

            this.choices = new ArrayList<>(choices.size());

            for (Material choice : choices) {
                Preconditions.checkArgument(choice != null, "Cannot have null choice");

                if (choice.isLegacy()) {
                    choice = Bukkit.getUnsafe().fromLegacy(new MaterialData(choice, (byte) 0), true);
                }

                Preconditions.checkArgument(!choice.isAir(), "Cannot have empty/air choice");
                Preconditions.checkArgument(choice.isItem(), "Cannot have non-item choice %s", choice); // Paper - validate material choice input to items
                this.choices.add(choice);
            }
        }

        @Override
        public boolean test(@NotNull ItemStack t) {
            for (Material match : choices) {
                if (t.getType() == match) {
                    return true;
                }
            }

            return false;
        }

        @NotNull
        @Override
        public ItemStack getItemStack() {
            ItemStack stack = new ItemStack(choices.get(0));

            // For compat
            if (choices.size() > 1) {
                stack.setDurability(Short.MAX_VALUE);
            }

            return stack;
        }

        @NotNull
        public List<Material> getChoices() {
            return Collections.unmodifiableList(choices);
        }

        @NotNull
        @Override
        public MaterialChoice clone() {
            try {
                MaterialChoice clone = (MaterialChoice) super.clone();
                clone.choices = new ArrayList<>(choices);
                return clone;
            } catch (CloneNotSupportedException ex) {
                throw new AssertionError(ex);
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.choices);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MaterialChoice other = (MaterialChoice) obj;
            if (!Objects.equals(this.choices, other.choices)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "MaterialChoice{" + "choices=" + choices + '}';
        }

        // Paper start - check valid ingredients
        @Override
        public @NotNull RecipeChoice validate(final boolean allowEmptyRecipes) {
            if (this.choices.stream().anyMatch(Material::isAir)) {
                throw new IllegalArgumentException("RecipeChoice.MaterialChoice cannot contain air");
            }
            return this;
        }
        // Paper end - check valid ingredients
    }

    /**
     * Represents a choice that will be valid only if one of the stacks is
     * exactly matched (aside from stack size).
     */
    public static class ExactChoice implements RecipeChoice {

        private List<ItemStack> choices;

        /**
         * @deprecated Use {@link RecipeChoice#exactChoice(ItemStack, ItemStack...)} instead
         */
        @Deprecated(since = "1.21.4", forRemoval = true)
        public ExactChoice(@NotNull ItemStack stack) {
            this(Arrays.asList(stack));
        }

        /**
         * @deprecated Use {@link RecipeChoice#exactChoice(ItemStack, ItemStack...)} instead
         */
        @Deprecated(since = "1.21.4", forRemoval = true)
        public ExactChoice(@NotNull ItemStack... stacks) {
            this(Arrays.asList(stacks));
        }

        /**
         * @deprecated Use {@link RecipeChoice#exactChoice(List)} instead
         */
        @Deprecated(since = "1.21.4", forRemoval = true)
        public ExactChoice(@NotNull List<ItemStack> choices) {
            Preconditions.checkArgument(choices != null, "choices");
            Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
            for (ItemStack choice : choices) {
                Preconditions.checkArgument(choice != null, "Cannot have null choice");
                Preconditions.checkArgument(!choice.getType().isAir(), "Cannot have empty/air choice");
            }

            this.choices = new ArrayList<>(choices);
        }

        @NotNull
        @Override
        public ItemStack getItemStack() {
            return choices.get(0).clone();
        }

        @NotNull
        public List<ItemStack> getChoices() {
            return Collections.unmodifiableList(choices);
        }

        @NotNull
        @Override
        public ExactChoice clone() {
            try {
                ExactChoice clone = (ExactChoice) super.clone();
                // Paper start - properly clone
                clone.choices = new ArrayList<>(this.choices.size());
                for (ItemStack choice : this.choices) {
                    clone.choices.add(choice.clone());
                }
                // Paper end - properly clone
                return clone;
            } catch (CloneNotSupportedException ex) {
                throw new AssertionError(ex);
            }
        }

        @Override
        public boolean test(@NotNull ItemStack t) {
            for (ItemStack match : choices) {
                if (t.isSimilar(match)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.choices);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExactChoice other = (ExactChoice) obj;
            if (!Objects.equals(this.choices, other.choices)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "ExactChoice{" + "choices=" + choices + '}';
        }

        // Paper start - check valid ingredients
        @Override
        public @NotNull RecipeChoice validate(final boolean allowEmptyRecipes) {
            if (this.choices.stream().anyMatch(s -> s.getType().isAir())) {
                throw new IllegalArgumentException("RecipeChoice.ExactChoice cannot contain air");
            }
            return this;
        }
        // Paper end - check valid ingredients
    }

    /**
     * Represents a choice that will be valid only if an item matches the
     * given predicate.
     */
    @ApiStatus.NonExtendable
    interface PredicateChoice extends RecipeChoice {

    }
}
