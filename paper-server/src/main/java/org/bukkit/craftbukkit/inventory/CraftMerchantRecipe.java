package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchantRecipe extends MerchantRecipe {

    private final net.minecraft.world.item.trading.MerchantOffer handle;

    public CraftMerchantRecipe(net.minecraft.world.item.trading.MerchantOffer merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.result), 0);
        this.handle = merchantRecipe;
        this.addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.baseCostA.itemStack()));
        merchantRecipe.costB.ifPresent((costB) -> this.addIngredient(CraftItemStack.asBukkitCopy(costB.itemStack())));
    }

    @Deprecated
    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier) {
        // Paper start - add ignoreDiscounts param
        this(result, uses, maxUses, experienceReward, experience, priceMultiplier, 0, 0, false);
    }
    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier, boolean ignoreDiscounts) {
        this(result, uses, maxUses, experienceReward, experience, priceMultiplier, 0, 0, ignoreDiscounts);
    }

    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier, int demand, int specialPrice) {
        this(result, uses, maxUses, experienceReward, experience, priceMultiplier, demand, specialPrice, false);
    }
    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward, int experience, float priceMultiplier, int demand, int specialPrice, boolean ignoreDiscounts) {
        super(result, uses, maxUses, experienceReward, experience, priceMultiplier, demand, specialPrice, ignoreDiscounts);
        // Paper end
        this.handle = new net.minecraft.world.item.trading.MerchantOffer(
                new ItemCost(Items.AIR),
                Optional.empty(),
                CraftItemStack.asNMSCopy(result),
                uses,
                maxUses,
                experience,
                priceMultiplier,
                demand,
                ignoreDiscounts, // Paper - add ignoreDiscounts param
                this
        );
        this.setSpecialPrice(specialPrice);
        this.setExperienceReward(experienceReward);
    }

    @Override
    public int getSpecialPrice() {
        return this.handle.getSpecialPriceDiff();
    }

    @Override
    public void setSpecialPrice(int specialPrice) {
        this.handle.specialPriceDiff = specialPrice;
    }

    @Override
    public int getDemand() {
        return this.handle.demand;
    }

    @Override
    public void setDemand(int demand) {
        this.handle.demand = demand;
    }

    @Override
    public int getUses() {
        return this.handle.uses;
    }

    @Override
    public void setUses(int uses) {
        this.handle.uses = uses;
    }

    @Override
    public int getMaxUses() {
        return this.handle.maxUses;
    }

    @Override
    public void setMaxUses(int maxUses) {
        this.handle.maxUses = maxUses;
    }

    @Override
    public boolean hasExperienceReward() {
        return this.handle.rewardExp;
    }

    @Override
    public void setExperienceReward(boolean flag) {
        this.handle.rewardExp = flag;
    }

    @Override
    public int getVillagerExperience() {
        return this.handle.xp;
    }

    @Override
    public void setVillagerExperience(int villagerExperience) {
        this.handle.xp = villagerExperience;
    }

    @Override
    public float getPriceMultiplier() {
        return this.handle.priceMultiplier;
    }

    @Override
    public void setPriceMultiplier(float priceMultiplier) {
        this.handle.priceMultiplier = priceMultiplier;
    }

    // Paper start
    @Override
    public boolean shouldIgnoreDiscounts() {
        return this.handle.ignoreDiscounts;
    }

    @Override
    public void setIgnoreDiscounts(boolean ignoreDiscounts) {
        this.handle.ignoreDiscounts = ignoreDiscounts;
    }
    // Paper end

    public net.minecraft.world.item.trading.MerchantOffer toMinecraft() {
        List<ItemStack> ingredients = this.getIngredients();
        Preconditions.checkState(!ingredients.isEmpty(), "No offered ingredients");
        net.minecraft.world.item.ItemStack baseCostA = CraftItemStack.asNMSCopy(ingredients.get(0));
        DataComponentExactPredicate baseCostAPredicate = DataComponentExactPredicate.allOf(PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, baseCostA.getComponentsPatch()));
        this.handle.baseCostA = new ItemCost(baseCostA.getItemHolder(), baseCostA.getCount(), baseCostAPredicate, baseCostA);
        if (ingredients.size() > 1) {
            net.minecraft.world.item.ItemStack costB = CraftItemStack.asNMSCopy(ingredients.get(1));
            DataComponentExactPredicate costBPredicate = DataComponentExactPredicate.allOf(PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, costB.getComponentsPatch()));
            this.handle.costB = Optional.of(new ItemCost(costB.getItemHolder(), costB.getCount(), costBPredicate, costB));
        } else {
            this.handle.costB = Optional.empty();
        }
        return this.handle;
    }

    public static CraftMerchantRecipe fromBukkit(MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe) recipe;
        } else {
            CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier(), recipe.getDemand(), recipe.getSpecialPrice(), recipe.shouldIgnoreDiscounts()); // Paper - shouldIgnoreDiscounts
            craft.setIngredients(recipe.getIngredients());

            return craft;
        }
    }
}
