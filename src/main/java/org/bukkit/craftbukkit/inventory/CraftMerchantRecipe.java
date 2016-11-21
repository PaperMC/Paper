package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchantRecipe extends MerchantRecipe {

    private final net.minecraft.server.MerchantRecipe handle;

    public CraftMerchantRecipe(net.minecraft.server.MerchantRecipe merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.sellingItem), 0);
        this.handle = merchantRecipe;
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.buyingItem1));
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.buyingItem2));
    }

    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward) {
        super(result, uses, maxUses, experienceReward);
        this.handle = new net.minecraft.server.MerchantRecipe(
                net.minecraft.server.ItemStack.a,
                net.minecraft.server.ItemStack.a,
                CraftItemStack.asNMSCopy(result),
                uses,
                maxUses,
                this
        );
    }

    @Override
    public int getUses() {
        return handle.uses;
    }

    @Override
    public void setUses(int uses) {
        handle.uses = uses;
    }

    @Override
    public int getMaxUses() {
        return handle.maxUses;
    }

    @Override
    public void setMaxUses(int maxUses) {
        handle.maxUses = maxUses;
    }

    @Override
    public boolean hasExperienceReward() {
        return handle.rewardExp;
    }

    @Override
    public void setExperienceReward(boolean flag) {
        handle.rewardExp = flag;
    }

    public net.minecraft.server.MerchantRecipe toMinecraft() {
        List<ItemStack> ingredients = getIngredients();
        Preconditions.checkState(!ingredients.isEmpty(), "No offered ingredients");
        handle.buyingItem1 = CraftItemStack.asNMSCopy(ingredients.get(0));
        if (ingredients.size() > 1) {
            handle.buyingItem2 = CraftItemStack.asNMSCopy(ingredients.get(1));
        }
        return handle;
    }

    public static CraftMerchantRecipe fromBukkit(MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe) recipe;
        } else {
            CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward());
            craft.setIngredients(recipe.getIngredients());

            return craft;
        }
    }
}
