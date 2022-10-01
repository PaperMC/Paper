package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchantRecipe extends MerchantRecipe {

    private final net.minecraft.world.item.trading.MerchantRecipe handle;

    public CraftMerchantRecipe(net.minecraft.world.item.trading.MerchantRecipe merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.result), 0);
        this.handle = merchantRecipe;
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.baseCostA));
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.costB));
    }

    @Deprecated
    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier) {
        this(result, uses, maxUses, experienceReward, experience, priceMultiplier, 0, 0);
    }

    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier, int demand, int specialPrice) {
        super(result, uses, maxUses, experienceReward, experience, priceMultiplier, demand, specialPrice);
        this.handle = new net.minecraft.world.item.trading.MerchantRecipe(
                net.minecraft.world.item.ItemStack.EMPTY,
                net.minecraft.world.item.ItemStack.EMPTY,
                CraftItemStack.asNMSCopy(result),
                uses,
                maxUses,
                experience,
                priceMultiplier,
                demand,
                this
        );
        this.setSpecialPrice(specialPrice);
        this.setExperienceReward(experienceReward);
    }

    @Override
    public int getSpecialPrice() {
        return handle.getSpecialPriceDiff();
    }

    @Override
    public void setSpecialPrice(int specialPrice) {
        handle.specialPriceDiff = specialPrice;
    }

    @Override
    public int getDemand() {
        return handle.demand;
    }

    @Override
    public void setDemand(int demand) {
        handle.demand = demand;
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

    @Override
    public int getVillagerExperience() {
        return handle.xp;
    }

    @Override
    public void setVillagerExperience(int villagerExperience) {
        handle.xp = villagerExperience;
    }

    @Override
    public float getPriceMultiplier() {
        return handle.priceMultiplier;
    }

    @Override
    public void setPriceMultiplier(float priceMultiplier) {
        handle.priceMultiplier = priceMultiplier;
    }

    public net.minecraft.world.item.trading.MerchantRecipe toMinecraft() {
        List<ItemStack> ingredients = getIngredients();
        Preconditions.checkState(!ingredients.isEmpty(), "No offered ingredients");
        handle.baseCostA = CraftItemStack.asNMSCopy(ingredients.get(0));
        if (ingredients.size() > 1) {
            handle.costB = CraftItemStack.asNMSCopy(ingredients.get(1));
        }
        return handle;
    }

    public static CraftMerchantRecipe fromBukkit(MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe) recipe;
        } else {
            CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier(), recipe.getDemand(), recipe.getSpecialPrice());
            craft.setIngredients(recipe.getIngredients());

            return craft;
        }
    }
}
