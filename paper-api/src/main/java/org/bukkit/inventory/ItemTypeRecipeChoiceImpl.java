package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.ItemTypeKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.jspecify.annotations.NullMarked;

@NullMarked
record ItemTypeRecipeChoiceImpl(RegistryKeySet<ItemType> itemTypes) implements RecipeChoice.ItemTypeChoice {

    ItemTypeRecipeChoiceImpl {
        Preconditions.checkArgument(!itemTypes.isEmpty(), "ItemTypeChoice cannot be empty");
        Preconditions.checkArgument(!(itemTypes.contains(ItemTypeKeys.AIR)), "ItemTypeChoice cannot contain minecraft:air");
    }

    @Override
    public ItemStack getItemStack() {
        throw new UnsupportedOperationException("ItemTypeChoice does not support this");
    }

    @Override
    public RecipeChoice clone() {
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
}
