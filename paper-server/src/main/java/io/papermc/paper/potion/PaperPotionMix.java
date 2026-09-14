package io.papermc.paper.potion;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPotionMix(ItemStack result, Predicate<ItemStack> input, Predicate<ItemStack> ingredient) {

    public PaperPotionMix(final PotionMix potionMix) {
        this(CraftItemStack.asNMSCopy(potionMix.getResult()), CraftRecipe.toIngredient(potionMix.getInput(), true), CraftRecipe.toIngredient(potionMix.getIngredient(), true));
    }
}
