package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a merchant's trade.
 * <p>
 * Trades can take one or two ingredients, and provide one result. The
 * ingredients' ItemStack amounts are respected in the trade.
 * <p>
 * A trade has a maximum number of uses. A {@link Villager} may periodically
 * replenish its trades by resetting the {@link #getUses uses} of its merchant
 * recipes to <code>0</code>, allowing them to be used again.
 * <p>
 * A trade may or may not reward experience for being completed.
 * <p>
 * During trades, the {@link MerchantRecipe} dynamically adjusts the amount of
 * its first ingredient based on the following criteria:
 * <ul>
 * <li>{@link #getDemand() Demand}: This value is periodically updated by the
 * villager that owns this merchant recipe based on how often the recipe has
 * been used since it has been last restocked in relation to its
 * {@link #getMaxUses maximum uses}. The amount by which the demand influences
 * the amount of the first ingredient is scaled by the recipe's
 * {@link #getPriceMultiplier price multiplier}, and can never be below zero.
 * <li>{@link #getSpecialPrice() Special price}: This value is dynamically
 * updated whenever a player starts and stops trading with a villager that owns
 * this merchant recipe. It is based on the player's individual reputation with
 * the villager, and the player's currently active status effects (see
 * {@link PotionEffectType#HERO_OF_THE_VILLAGE}). The influence of the player's
 * reputation on the special price is scaled by the recipe's
 * {@link #getPriceMultiplier price multiplier}.
 * </ul>
 * The adjusted amount of the first ingredient is calculated by adding up the
 * original amount of the first ingredient, the demand scaled by the recipe's
 * {@link #getPriceMultiplier price multiplier} and truncated to the next lowest
 * integer value greater than or equal to 0, and the special price, and then
 * constraining the resulting value between <code>1</code> and the item stack's
 * {@link ItemStack#getMaxStackSize() maximum stack size}.
 */
public class MerchantRecipe implements Recipe {

    private ItemStack result;
    private List<ItemStack> ingredients = new ArrayList<ItemStack>();
    private int uses;
    private int maxUses;
    private boolean experienceReward;
    private int specialPrice;
    private int demand;
    private int villagerExperience;
    private float priceMultiplier;

    public MerchantRecipe(@NotNull ItemStack result, int maxUses) {
        this(result, 0, maxUses, false);
    }

    public MerchantRecipe(@NotNull ItemStack result, int uses, int maxUses, boolean experienceReward) {
        this(result, uses, maxUses, experienceReward, 0, 0.0F, 0, 0);
    }

    public MerchantRecipe(@NotNull ItemStack result, int uses, int maxUses, boolean experienceReward, int villagerExperience, float priceMultiplier) {
        this(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier, 0, 0);
    }

    public MerchantRecipe(@NotNull ItemStack result, int uses, int maxUses, boolean experienceReward, int villagerExperience, float priceMultiplier, int demand, int specialPrice) {
        this.result = result;
        this.uses = uses;
        this.maxUses = maxUses;
        this.experienceReward = experienceReward;
        this.villagerExperience = villagerExperience;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
        this.specialPrice = specialPrice;
    }

    @NotNull
    @Override
    public ItemStack getResult() {
        return result;
    }

    public void addIngredient(@NotNull ItemStack item) {
        Preconditions.checkState(ingredients.size() < 2, "MerchantRecipe can only have maximum 2 ingredients");
        ingredients.add(item.clone());
    }

    public void removeIngredient(int index) {
        ingredients.remove(index);
    }

    public void setIngredients(@NotNull List<ItemStack> ingredients) {
        Preconditions.checkState(ingredients.size() <= 2, "MerchantRecipe can only have maximum 2 ingredients");
        this.ingredients = new ArrayList<ItemStack>();
        for (ItemStack item : ingredients) {
            this.ingredients.add(item.clone());
        }
    }

    @NotNull
    public List<ItemStack> getIngredients() {
        List<ItemStack> copy = new ArrayList<ItemStack>();
        for (ItemStack item : ingredients) {
            copy.add(item.clone());
        }
        return copy;
    }

    /**
     * Gets the {@link #adjust(ItemStack) adjusted} first ingredient.
     *
     * @return the adjusted first ingredient, or <code>null</code> if this
     * recipe has no ingredients
     * @see #adjust(ItemStack)
     */
    @Nullable
    public ItemStack getAdjustedIngredient1() {
        if (this.ingredients.isEmpty()) {
            return null;
        }

        ItemStack firstIngredient = this.ingredients.get(0).clone();
        adjust(firstIngredient);
        return firstIngredient;
    }

    /**
     * Modifies the amount of the given {@link ItemStack} in the same way as
     * MerchantRecipe dynamically adjusts the amount of the first ingredient
     * during trading.
     * <br>
     * This is calculated by adding up the original amount of the item, the
     * demand scaled by the recipe's
     * {@link #getPriceMultiplier price multiplier} and truncated to the next
     * lowest integer value greater than or equal to 0, and the special price,
     * and then constraining the resulting value between <code>1</code> and the
     * {@link ItemStack}'s {@link ItemStack#getMaxStackSize()
     * maximum stack size}.
     *
     * @param itemStack the item to adjust
     */
    public void adjust(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            return;
        }

        int amount = itemStack.getAmount();
        int demandAdjustment = Math.max(0, NumberConversions.floor((float) (amount * getDemand()) * getPriceMultiplier()));
        itemStack.setAmount(Math.max(1, Math.min(itemStack.getMaxStackSize(), amount + demandAdjustment + getSpecialPrice())));
    }

    /**
     * Get the demand for this trade.
     *
     * @return the demand
     */
    public int getDemand() {
        return demand;
    }

    /**
     * Set the demand for this trade.
     *
     * @param demand the new demand
     */
    public void setDemand(int demand) {
        this.demand = demand;
    }

    /**
     * Get the special price for this trade.
     *
     * @return special price value
     */
    public int getSpecialPrice() {
        return specialPrice;
    }

    /**
     * Set the special price for this trade.
     *
     * @param specialPrice special price value
     */
    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
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
     * Whether to reward experience to the player for the trade.
     *
     * @return whether to reward experience to the player for completing this
     * trade
     */
    public boolean hasExperienceReward() {
        return experienceReward;
    }

    /**
     * Set whether to reward experience to the player for the trade.
     *
     * @param flag whether to reward experience to the player for completing
     * this trade
     */
    public void setExperienceReward(boolean flag) {
        this.experienceReward = flag;
    }

    /**
     * Gets the amount of experience the villager earns from this trade.
     *
     * @return villager experience
     */
    public int getVillagerExperience() {
        return villagerExperience;
    }

    /**
     * Sets the amount of experience the villager earns from this trade.
     *
     * @param villagerExperience new experience amount
     */
    public void setVillagerExperience(int villagerExperience) {
        this.villagerExperience = villagerExperience;
    }

    /**
     * Gets the price multiplier for the cost of this trade.
     *
     * @return price multiplier
     */
    public float getPriceMultiplier() {
        return priceMultiplier;
    }

    /**
     * Sets the price multiplier for the cost of this trade.
     *
     * @param priceMultiplier new price multiplier
     */
    public void setPriceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }
}
