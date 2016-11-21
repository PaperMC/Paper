package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a merchant's trade.
 *
 * Trades can take one or two ingredients, and provide one result. The
 * ingredients' Itemstack amounts are respected in the trade.
 * <br>
 * A trade has a limited number of uses, after which the trade can no longer be
 * used, unless the player uses a different trade, which will cause its maximum
 * uses to increase.
 * <br>
 * A trade may or may not reward experience for being completed.
 *
 * @see org.bukkit.event.entity.VillagerReplenishTradeEvent
 */
public class MerchantRecipe implements Recipe {

    private ItemStack result;
    private List<ItemStack> ingredients = new ArrayList<ItemStack>();
    private int uses;
    private int maxUses;
    private boolean experienceReward;

    public MerchantRecipe(ItemStack result, int maxUses) {
        this(result, 0, maxUses, false);
    }

    public MerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward) {
        this.result = result;
        this.uses = uses;
        this.maxUses = maxUses;
        this.experienceReward = experienceReward;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    public void addIngredient(ItemStack item) {
        Preconditions.checkState(ingredients.size() < 2, "MerchantRecipe can only have 2 ingredients");
        ingredients.add(item.clone());
    }

    public void removeIngredient(int index) {
        ingredients.remove(index);
    }

    public void setIngredients(List<ItemStack> ingredients) {
        this.ingredients = new ArrayList<ItemStack>();
        for (ItemStack item : ingredients) {
            this.ingredients.add(item.clone());
        }
    }

    public List<ItemStack> getIngredients() {
        List<ItemStack> copy = new ArrayList<ItemStack>();
        for (ItemStack item : ingredients) {
            copy.add(item.clone());
        }
        return copy;
    }

    /**
     * Get the number of times this trade has been used.
     *
     * @return the number of uses
     */
    public int getUses() {
        return uses;
    }

    /**
     * Set the number of times this trade has been used.
     *
     * @param uses the number of uses
     */
    public void setUses(int uses) {
        this.uses = uses;
    }

    /**
     * Get the maximum number of uses this trade has.
     * <br>
     * The maximum uses of this trade may increase when a player trades with the
     * owning merchant.
     *
     * @return the maximum number of uses
     */
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * Set the maximum number of uses this trade has.
     *
     * @param maxUses the maximum number of time this trade can be used
     */
    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    /**
     * Whether to reward experience for the trade.
     *
     * @return whether to reward experience for completing this trade
     */
    public boolean hasExperienceReward() {
        return experienceReward;
    }

    /**
     * Set whether to reward experience for the trade.
     *
     * @param flag whether to reward experience for completing this trade
     */
    public void setExperienceReward(boolean flag) {
        this.experienceReward = flag;
    }
}
