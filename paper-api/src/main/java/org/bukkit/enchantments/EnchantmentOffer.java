package org.bukkit.enchantments;

import com.google.common.base.Preconditions;
import io.papermc.paper.InternalAPIBridge;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;
import org.checkerframework.checker.index.qual.Positive;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requirePositive;

/**
 * A class for the available enchantment offers in the enchantment table.
 */
@NullMarked
public class EnchantmentOffer {

    private Enchantment enchantment;
    private int enchantmentLevel;
    private int cost;

    public EnchantmentOffer(final Enchantment enchantment, final int enchantmentLevel, final int cost) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
        this.cost = cost;
    }

    /**
     * Get the type of the enchantment.
     *
     * @return type of enchantment
     */
    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    /**
     * Sets the type of the enchantment.
     *
     * @param enchantment type of the enchantment
     */
    public void setEnchantment(final Enchantment enchantment) {
        Preconditions.checkArgument(enchantment != null, "The enchantment may not be null!");

        this.enchantment = enchantment;
    }

    /**
     * Gets the level of the enchantment.
     *
     * @return level of the enchantment
     */
    public int getEnchantmentLevel() {
        return this.enchantmentLevel;
    }

    /**
     * Sets the level of the enchantment.
     *
     * @param enchantmentLevel level of the enchantment
     */
    public void setEnchantmentLevel(final @Positive int enchantmentLevel) {
        requirePositive(enchantmentLevel, "enchantmentLevel");

        this.enchantmentLevel = enchantmentLevel;
    }

    /**
     * Gets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @return cost for this enchantment
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Sets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @param cost cost for this enchantment
     */
    public void setCost(final @Positive int cost) {
        requirePositive(cost, "cost");

        this.cost = cost;
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final EnchantmentOffer that = (EnchantmentOffer) o;
        return this.getEnchantmentLevel() == that.getEnchantmentLevel() && this.getCost() == that.getCost() && Objects.equals(this.getEnchantment(), that.getEnchantment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEnchantment(), this.getEnchantmentLevel(), this.getCost());
    }

    /**
     * Rolls a new set of enchantment offers based on a target item stack, a seed and a bookshelf count.
     *
     * @param targetStack    the itemstack for which the enchantment offers are rolled.
     * @param seed           the seed used for random selection of enchantments.
     *                       Mirrors {@link org.bukkit.inventory.view.EnchantmentView#getEnchantmentSeed()}.
     * @param bookshelfCount the number of virtual bookshelves to consider when rolling the enchantments.
     * @param offerAmount    the amount of enchantment offers to roll.
     * @return an array of enchantment offers with size {@code offerAmount}.
     */
    public static @Nullable EnchantmentOffer[] rollEnchantmentOffers(
        final ItemStack targetStack,
        final int seed,
        final int bookshelfCount,
        final int offerAmount
    ) {
        return InternalAPIBridge.get().rollEnchantmentOffers(targetStack, seed, bookshelfCount, offerAmount);
    }
}
