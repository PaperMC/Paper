package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Material;

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
     * @deprecated for compatability only
     */
    @Deprecated
    ItemStack getItemStack();

    RecipeChoice clone();

    /**
     * Represents a choice of multiple matching Materials.
     */
    public static class MaterialChoice implements RecipeChoice {

        private List<Material> choices;

        public MaterialChoice(Material choice) {
            this(Arrays.asList(choice));
        }

        public MaterialChoice(Material... choices) {
            this(Arrays.asList(choices));
        }

        public MaterialChoice(List<Material> choices) {
            Preconditions.checkArgument(choices != null, "choices");
            Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
            for (Material choice : choices) {
                Preconditions.checkArgument(choice != null, "Cannot have null choice");
            }

            this.choices = choices;
        }

        @Override
        public boolean test(ItemStack t) {
            for (Material match : choices) {
                if (t.getType() == match) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public ItemStack getItemStack() {
            ItemStack stack = new ItemStack(choices.get(0));

            // For compat
            if (choices.size() > 1) {
                stack.setDurability(Short.MAX_VALUE);
            }

            return stack;
        }

        public List<Material> getChoices() {
            return Collections.unmodifiableList(choices);
        }

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
     * <b>Not valid for shapeless recipes</b>
     *
     * @deprecated draft API
     */
    @Deprecated
    public static class ExactChoice implements RecipeChoice {

        private List<ItemStack> choices;

        public ExactChoice(ItemStack stack) {
            this(Arrays.asList(stack));
        }

        public ExactChoice(ItemStack... stacks) {
            this(Arrays.asList(stacks));
        }

        public ExactChoice(List<ItemStack> choices) {
            Preconditions.checkArgument(choices != null, "choices");
            Preconditions.checkArgument(!choices.isEmpty(), "Must have at least one choice");
            for (ItemStack choice : choices) {
                Preconditions.checkArgument(choice != null, "Cannot have null choice");
            }

            this.choices = choices;
        }

        @Override
        public ItemStack getItemStack() {
            return choices.get(0).clone();
        }

        public List<ItemStack> getChoices() {
            return Collections.unmodifiableList(choices);
        }

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
        public boolean test(ItemStack t) {
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
