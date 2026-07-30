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
import org.jspecify.annotations.NullMarked;

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
    private boolean ignoreDiscounts; // Paper

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
        // Paper start - add ignoreDiscounts param
        this(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier, demand, specialPrice, false);
    }
    public MerchantRecipe(@NotNull ItemStack result, int uses, int maxUses, boolean experienceReward, int villagerExperience, float priceMultiplier, boolean ignoreDiscounts) {
        this(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier, 0, 0, ignoreDiscounts);
    }
    public MerchantRecipe(@NotNull ItemStack result, int uses, int maxUses, boolean experienceReward, int villagerExperience, float priceMultiplier, int demand, int specialPrice, boolean ignoreDiscounts) {
        Preconditions.checkArgument(!result.isEmpty(), "Recipe cannot have an empty result."); // Paper
        this.ignoreDiscounts = ignoreDiscounts;
        // Paper end
        this.result = result;
        this.uses = uses;
        this.maxUses = maxUses;
        this.experienceReward = experienceReward;
        this.villagerExperience = villagerExperience;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
        this.specialPrice = specialPrice;
    }

    // Paper start - add copy ctor
    public MerchantRecipe(@NotNull MerchantRecipe recipe) {
        this(recipe.result.clone(), recipe.uses, recipe.maxUses, recipe.experienceReward, recipe.villagerExperience, recipe.priceMultiplier, recipe.demand, recipe.specialPrice, recipe.ignoreDiscounts);
        this.setIngredients(recipe.ingredients);
    }
    // Paper end

    @NotNull
    @Override
    public ItemStack getResult() {
        return result.clone(); // Paper
    }

    public void addIngredient(@NotNull ItemStack item) {
        Preconditions.checkState(ingredients.size() < 2, "MerchantRecipe can only have maximum 2 ingredients");
        Preconditions.checkArgument(!item.isEmpty(), "Recipe cannot have an empty itemstack ingredient."); // Paper
        ingredients.add(item.clone());
    }

    public void removeIngredient(int index) {
        ingredients.remove(index);
    }

    public void setIngredients(@NotNull List<ItemStack> ingredients) {
        Preconditions.checkState(ingredients.size() <= 2, "MerchantRecipe can only have maximum 2 ingredients");
        this.ingredients = new ArrayList<ItemStack>();
        for (ItemStack item : ingredients) {
            Preconditions.checkArgument(!item.isEmpty(), "Recipe cannot have an empty itemstack ingredient."); // Paper
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

    // Paper start
    /**
     * @return Whether all discounts on this trade should be ignored.
     */
    public boolean shouldIgnoreDiscounts() {
        return ignoreDiscounts;
    }

    /**
     * @param ignoreDiscounts Whether all discounts on this trade should be ignored.
     */
    public void setIgnoreDiscounts(boolean ignoreDiscounts) {
        this.ignoreDiscounts = ignoreDiscounts;
    }
    // Paper end
    
    /**
     * Creates a {@link Builder} instance to use for modifying values such as the result
     * item.<br>
     * This Builder class will contain all the values from this MerchantRecipe instance.
     *
     * @return New Builder class with values from this MerchantRecipe applied.
     */
    public Builder builder() {
        return new Builder(this);
    }
    
    /**
     * Builder class for creating a new {@link MerchantRecipe}.
     * This class allows the result ItemStack to be modified until the
     * {@link #build() the MerchantRecipe is build}.
     *
     * <p>A Builder copy of an existing MerchantRecipe can be obtained through
     * its {@link MerchantRecipe#builder() builder method}.
     */
    @NullMarked
    public static class Builder {
        private ItemStack result;
        private List<ItemStack> ingredients = new ArrayList<>();
        private int uses = 0;
        private int maxUses;
        private boolean experienceReward = false;
        private int specialPrice = 0;
        private int demand = 0;
        private int villagerExperience = 0;
        private float priceMultiplier = 0.0F;
        private boolean ignoreDiscounts = false;
        
        /**
         * Basic constructor to create a new Builder class from a provided result
         * ItemStack and maxUses integer.<br>
         * The created Builder will have all its other values be empty, zero or
         * false, depending on the type.
         *
         * @param result The ItemStack to use as the result for the MerchantRecipe.
         * @param maxUses Number of max uses for this MerchantRecipe
         */
        public Builder(ItemStack result, int maxUses) {
            Preconditions.checkArgument(!result.isEmpty(), "Result cannot be empty");
            this.result = result;
            this.maxUses = maxUses;
        }
        
        /**
         * Constructor for creating a Builder instance from an existing
         * {@link MerchantRecipe} instance.<br>
         * This Builder instance will have all its values set to what the provided
         * MerchantRecipe had set.
         *
         * @param merchantRecipe The MerchantRecipe instance to create a Builder instance from.
         */
        public Builder(MerchantRecipe merchantRecipe) {
            this.result = merchantRecipe.getResult();
            this.ingredients = merchantRecipe.getIngredients();
            this.uses = merchantRecipe.getUses();
            this.maxUses = merchantRecipe.getMaxUses();
            this.experienceReward = merchantRecipe.hasExperienceReward();
            this.specialPrice = merchantRecipe.getSpecialPrice();
            this.demand = merchantRecipe.getDemand();
            this.villagerExperience = merchantRecipe.getVillagerExperience();
            this.priceMultiplier = merchantRecipe.getPriceMultiplier();
            this.ignoreDiscounts = merchantRecipe.shouldIgnoreDiscounts();
        }
        
        /**
         * Sets the {@link ItemStack} to use as the result for this MerchantRecipe.
         *
         * @param result Result ItemStack for this MerchantRecipe.
         * @return This Builder for chaining purposes.
         */
        public Builder setResult(ItemStack result) {
            Preconditions.checkArgument(!result.isEmpty(), "Result cannot be empty.");
            this.result = result.clone();
            return this;
        }
        
        /**
         * Adds an {@link ItemStack} as a new Ingredient for this MerchantRecipe.<br>
         * The position of the ItemStack in the list determines which ingredient
         * it is and there can't be more than 2 in total.
         *
         * @param item The ItemStack to add.
         * @return This Builder for chaining purposes.
         */
        public Builder addIngredient(ItemStack item) {
            Preconditions.checkState(ingredients.size() < 2, "Recipe can only have maximum 2 ingredients");
            Preconditions.checkArgument(!item.isEmpty(), "Recipe cannot have an empty itemstack ingredient.");
            ingredients.add(item.clone());
            return this;
        }
        
        /**
         * Removes an ingredient from the provided index in the ingredients list.
         *
         * @param index position in the list to remove the ItemStack from.
         * @return This Builder for chaining purposes.
         */
        public Builder removeIngredient(int index) {
            ingredients.remove(index);
            return this;
        }
        
        /**
         * Sets the {@link ItemStack List of ItemStacks} to use for this MerchantRecipe.
         *
         * @param ingredients List of ItemStacks to use as the ingredients.
         * @return This Builder for chaining purposes.
         */
        public Builder setIngredients(List<ItemStack> ingredients) {
            Preconditions.checkState(ingredients.size() <= 2, "Recipe can only have maximum 2 ingredients");
            this.ingredients = new ArrayList<>();
            for (ItemStack item : ingredients) {
                Preconditions.checkArgument(!item.isEmpty(), "Recipe cannot have an empty itemstack ingredient.");
                this.ingredients.add(item.clone());
            }
            return this;
        }
        
        /**
         * Resets the demand for this MerchantRecipe to 0.<br>
         * This is a convenience method for {@code setDemand(0)}.
         *
         * @return This Builder for chaining purposes.
         */
        public Builder resetDemand() {
            return setDemand(0);
        }
        
        /**
         * Sets the demand for this MerchantRecipe.
         * @param demand Demant for this MerchantRecipe.
         * @return This Builder for chaining purposes.
         */
        public Builder setDemand(int demand) {
            this.demand = demand;
            return this;
        }
        
        /**
         * Resets the special price for this MerchantRecipe to 0.<br>
         * This is a convenience method for {@code setSpecialPrice(0)}.
         *
         * @return This Builder for chaining purposes.
         */
        public Builder resetSpecialPrice() {
            return setSpecialPrice(0);
        }
        
        /**
         * Sets the special price for this MerchantRecipe.
         * @param specialPrice Special Price for this MerchantRecipe.
         * @return This Builder for chaining purposes.
         */
        public Builder setSpecialPrice(int specialPrice) {
            this.specialPrice = specialPrice;
            return this;
        }
        
        /**
         * Resets the Use count for this MerchantRecipe to 0.<br>
         * This is a convenience method for {@code setUses(0)}.
         *
         * @return This Builder for chaining purposes.
         */
        public Builder resetUses() {
            return setUses(0);
        }
        
        /**
         * Sets the uses for this MerchantRecipe.
         *
         * @param uses Use count for this MerchantRecipe.
         * @return This Builder for chaining purposes.
         */
        public Builder setUses(int uses) {
            this.uses = uses;
            return this;
        }
        
        /**
         * Sets the max uses for this MerchantRecipe.
         *
         * @param maxUses Max use count for this MerchantRecipe.
         * @return This Builder for chaining purposes.
         */
        public Builder setMaxUses(int maxUses) {
            this.maxUses = maxUses;
            return this;
        }
        
        /**
         * Enables the MerchantRecipe to give experience to the player on
         * completing the trade.<br>
         * This is a convenience method for {@code setExperienceReward(true)}.
         *
         * @return This Builder for chaining purposes.
         */
        public Builder experienceReward() {
            return setExperienceReward(true);
        }
        
        /**
         * Sets whether this MerchantRecipe should give experience to a player
         * on completing the trade.
         *
         * @param experienceReward Setting whether the MerchantRecipe should give experience.
         * @return This Builder for chaining purposes.
         */
        public Builder setExperienceReward(boolean experienceReward) {
            this.experienceReward = experienceReward;
            return this;
        }
        
        /**
         * Sets amount of experience the Villager gains when completing a trade.
         *
         * @param villagerExperience Amount of experience the villager should gain.
         * @return This Builder for chaining purposes.
         */
        public Builder setVillageExperience(int villagerExperience) {
            this.villagerExperience = villagerExperience;
            return this;
        }
        
        /**
         * Sets the multiplier to apply on prices for this MerchantRecipe.
         *
         * @param priceMultiplier Multiplier to apply to this MerchantRecipe.
         * @return This Builder for chaining purposes.
         */
        public Builder setPriceMultiplier(float priceMultiplier) {
            this.priceMultiplier = priceMultiplier;
            return this;
        }
        
        /**
         * Enables this MerchantRecipe to ignore any discounts from cases like a
         * Player having the {@link PotionEffectType#HERO_OF_THE_VILLAGE Hero of the Village}
         * effect.<br>
         * This is a convenience method for {@code setIgnoreDiscounts(true)}.
         *
         * @return This Builder for chaining purposes.
         */
        public Builder ignoreDiscounts() {
            return setIgnoreDiscounts(true);
        }
        
        /**
         * Sets whether this MerchantRecipe should ignore discounts applued from
         * cases like a Player having the {@link PotionEffectType#HERO_OF_THE_VILLAGE Hero of the Village}
         * effect.
         *
         * @param ignoreDiscounts Whether this MerchantRecipe should ignore discounts.
         * @return This Builder for chaining purposes.
         */
        public Builder setIgnoreDiscounts(boolean ignoreDiscounts) {
            this.ignoreDiscounts = ignoreDiscounts;
            return this;
        }
        
        /**
         * Creates a new {@link MerchantRecipe} instance with the values of this class
         * applies.
         *
         * @return New MerchantRecipe class.
         */
        public MerchantRecipe build() {
            return new MerchantRecipe(result, uses, maxUses, experienceReward,
                villagerExperience, priceMultiplier, demand, specialPrice, ignoreDiscounts);
        }
    }
}
