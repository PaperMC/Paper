package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
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
import org.jspecify.annotations.NullMarked;

/**
 * Represents a potential item match within a recipe. All choices within a
 * recipe must be satisfied for it to be craftable. Choices must never be
 * null or air.
 *
 * <b>This class is not legal for implementation by plugins!</b>
 */
@NullMarked
@ApiStatus.NonExtendable
public interface RecipeChoice extends Predicate<ItemStack>, Cloneable {

    /**
     * An "empty" recipe choice. Only valid as a recipe choice in
     * specific places. Check the javadocs of a method before using it
     * to be sure it's valid for that recipe and ingredient type.
     *
     * @return the empty recipe choice
     */
    static RecipeChoice empty() {
        return EmptyRecipeChoice.INSTANCE;
    }

    /**
     * Creates a new recipe choice based on a collection of {@link ItemType}s.
     *
     * @param itemType the first item type to match
     * @param itemTypes other item types to match.
     * @return a new recipe choice
     */
    @Contract(pure = true, value = "_, _ -> new")
    static ItemTypeChoice itemType(final ItemType itemType, final ItemType ... itemTypes) {
        final List<ItemType> types = new ArrayList<>();
        types.add(itemType);
        types.addAll(Arrays.asList(itemTypes));
        return itemType(RegistrySet.keySetFromValues(RegistryKey.ITEM, types));
    }

    /**
     * Creates a new recipe choice based on a {@link RegistryKeySet} of item types.
     * Can either be created via {@link RegistryKeySet#keySet(RegistryKey, TypedKey[])}
     * or obtained from {@link org.bukkit.Registry#getTag(TagKey)}.
     *
     * @param itemTypes the item types to match
     * @return a new recipe choice
     */
    @Contract(pure = true, value = "_ -> new")
    static ItemTypeChoice itemType(final RegistryKeySet<ItemType> itemTypes) {
        return new ItemTypeRecipeChoiceImpl(itemTypes);
    }

    /**
     * Gets a single item stack representative of this stack choice.
     *
     * @return a single representative item
     * @deprecated for compatibility only
     */
    @Deprecated(since = "1.13.1")
    ItemStack getItemStack();

    RecipeChoice clone();

    @Override
    boolean test(ItemStack itemStack);

    // Paper start - check valid ingredients
    @org.jetbrains.annotations.ApiStatus.Internal
    default RecipeChoice validate(final boolean allowEmptyRecipes) {
        return this;
    }
    // Paper end - check valid ingredients

    /**
     * Represents a choice of multiple matching Materials.
     * @apiNote recommended to use {@link ItemTypeChoice}
     */
    @ApiStatus.Obsolete(since = "1.21.11")
    sealed class MaterialChoice implements RecipeChoice permits ItemTypeRecipeChoiceImpl {

        protected MaterialChoice() {
        }

        private List<Material> choices;

        public MaterialChoice(Material choice) {
            this(Arrays.asList(choice));
        }

        public MaterialChoice(Material... choices) {
            this(Arrays.asList(choices));
        }

        /**
         * Constructs a MaterialChoice with the current values of the specified
         * tag.
         *
         * @param choices the tag
         */
        public MaterialChoice(Tag<Material> choices) {
            this(new ArrayList<>(java.util.Objects.requireNonNull(choices, "Cannot create a material choice with null tag").getValues())); // Paper - delegate to list ctor to make sure all checks are called
        }

        public MaterialChoice(List<Material> choices) {
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
        public boolean test(ItemStack t) {
            for (Material match : choices) {
                if (t.getType() == match) {
                    return true;
                }
            }

            return false;
        }

        @Override
        @Deprecated(since = "1.13.1")
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

        // Paper start - check valid ingredients
        @Override
        public RecipeChoice validate(final boolean allowEmptyRecipes) {
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
    final class ExactChoice implements RecipeChoice {

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
                Preconditions.checkArgument(!choice.getType().isAir(), "Cannot have empty/air choice");
            }

            this.choices = new ArrayList<>(choices);
        }

        @Override
        @Deprecated(since = "1.13.1")
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

        // Paper start - check valid ingredients
        @Override
        public RecipeChoice validate(final boolean allowEmptyRecipes) {
            if (this.choices.stream().anyMatch(s -> s.getType().isAir())) {
                throw new IllegalArgumentException("RecipeChoice.ExactChoice cannot contain air");
            }
            return this;
        }
        // Paper end - check valid ingredients
    }

    /**
     * Represents a choice that will be valid if the {@link ItemStack#getType()}
     * matches any of the item types in the set.
     *
     * @see #itemType(RegistryKeySet)
     * @see #itemType(ItemType, ItemType...)
     */
    sealed interface ItemTypeChoice extends RecipeChoice permits ItemTypeRecipeChoiceImpl {

        /**
         * Gets the set of item types that this choice will match.
         *
         * @return the set of item types
         */
        RegistryKeySet<ItemType> itemTypes();
    }
}
