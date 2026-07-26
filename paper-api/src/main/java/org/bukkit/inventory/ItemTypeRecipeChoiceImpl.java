package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.ItemTypeKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This class extends {@link org.bukkit.inventory.RecipeChoice.MaterialChoice} to maintain
 * compatibility when instances are returned from recipes.
 */
@NullMarked
final class ItemTypeRecipeChoiceImpl extends RecipeChoice.MaterialChoice implements RecipeChoice.ItemTypeChoice {

    private final RegistryKeySet<ItemType> itemTypes;
    private @Nullable List<Material> legacyChoices;

    ItemTypeRecipeChoiceImpl(final RegistryKeySet<ItemType> itemTypes) {
        Preconditions.checkArgument(!itemTypes.isEmpty(), "ItemTypeChoice cannot be empty");
        Preconditions.checkArgument(!(itemTypes.contains(ItemTypeKeys.AIR)), "ItemTypeChoice cannot contain minecraft:air");
        this.itemTypes = itemTypes;
    }

    @Override
    @Deprecated(since = "1.13.1")
    public ItemStack getItemStack() {
        final ItemType type = Registry.ITEM.getOrThrow(this.itemTypes.iterator().next());
        final ItemStack item = type.createItemStack();

        // legacy compat
        if (this.itemTypes.size() > 1) {
            item.setDurability(Short.MAX_VALUE);
        }

        return item;
    }

    @Override
    public ItemTypeRecipeChoiceImpl clone() {
        return new ItemTypeRecipeChoiceImpl(this.itemTypes);
    }

    @Override
    public boolean test(final ItemStack itemStack) {
        return this.itemTypes.contains(RegistryKey.ITEM.typedKey(itemStack.getType().key()));
    }

    @Override
    public RecipeChoice validate(final boolean allowEmptyRecipes) {
        Preconditions.checkArgument(!(this.itemTypes.contains(ItemTypeKeys.AIR)), "ItemTypeChoice cannot contain minecraft:air");
        return this;
    }

    @Override
    public RegistryKeySet<ItemType> itemTypes() {
        return this.itemTypes;
    }

    @Override
    public List<Material> getChoices() {
        if (this.legacyChoices == null) {
            final List<Material> choices = new ArrayList<>();
            for (final TypedKey<ItemType> itemTypeKey : this.itemTypes) {
                choices.add(Registry.MATERIAL.getOrThrow(itemTypeKey));
            }
            this.legacyChoices = Collections.unmodifiableList(choices);
        }
        return this.legacyChoices;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (ItemTypeRecipeChoiceImpl) obj;
        return Objects.equals(this.itemTypes, that.itemTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.itemTypes);
    }

    @Override
    public String toString() {
        return "ItemTypeRecipeChoiceImpl[" + "itemTypes=" + this.itemTypes + ']';
    }
}
