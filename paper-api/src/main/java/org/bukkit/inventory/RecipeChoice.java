package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a potential item match within a recipe. All choices within a
 * recipe must be satisfied for it to be craftable.
 *
 * <b>This class is not legal for implementation by plugins!</b>
 */
public interface RecipeChoice extends Predicate<ItemStack>, Cloneable {

    /**
     * Gets a single item stack representative of this stack choice.
     *
     * @return a single representative item
     * @deprecated for compatibility only
     */
    @Deprecated
    @NotNull
    ItemStack getItemStack();

    @NotNull
    RecipeChoice clone();

    @Override
    boolean test(@NotNull ItemStack itemStack);

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
            Preconditions.checkArgument(choices != null, "choices");
            this.choices = new ArrayList<>(choices.getValues());
        }

        public MaterialChoice(@NotNull List<Material> choices) {
            Preconditions.checkArgument(choices != null, "choices");
            Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
            for (Material choice : choices) {
                Preconditions.checkArgument(choice != null, "Cannot have null choice");
            }

            this.choices = new ArrayList<>(choices);
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
    }

    /**
     * Represents a choice that will be valid only one of the stacks is exactly
     * matched (aside from stack size).
     * <br>
     * <b>Only valid for shaped recipes</b>
     */
    public static class ExactChoice implements RecipeChoice {

        private List<ItemStack> choices;

        public ExactChoice(@NotNull ItemStack stack) {
            this(Arrays.asList(stack));
        }

        public ExactChoice(@NotNull ItemStack... stacks) {
            this(Arrays.asList(stacks));
        }

        public ExactChoice(@NotNull List<ItemStack> choices) {
            Preconditions.checkArgument(choices != null, "choices");
            Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
            for (ItemStack choice : choices) {
                Preconditions.checkArgument(choice != null, "Cannot have null choice");
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
                clone.choices = new ArrayList<>(choices);
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
    }
}
