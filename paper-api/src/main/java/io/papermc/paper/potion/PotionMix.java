package io.papermc.paper.potion;

import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a potion mix made in a Brewing Stand.
 */
@NullMarked
public final class PotionMix implements Keyed {

    private final NamespacedKey key;
    private final ItemStack result;
    private final RecipeChoice input;
    private final RecipeChoice ingredient;

    /**
     * Creates a new potion mix. Add it to the server with {@link org.bukkit.potion.PotionBrewer#addPotionMix(PotionMix)}.
     *
     * @param key a unique key for the mix
     * @param result the resulting itemstack that will appear in the 3 bottom slots
     * @param input the input placed into the bottom 3 slots
     * @param ingredient the ingredient placed into the top slot
     */
    public PotionMix(final NamespacedKey key, final ItemStack result, final RecipeChoice input, final RecipeChoice ingredient) {
        this.key = key;
        this.result = result.clone();
        this.input = input.clone();
        this.ingredient = ingredient.clone();
    }

    /**
     * Create a {@link RecipeChoice} based on a Predicate. These RecipeChoices are only
     * valid for {@link PotionMix}, not anywhere else RecipeChoices may be used.
     *
     * @param stackPredicate a predicate for an itemstack.
     * @return a new RecipeChoice
     */
    @Contract(value = "_ -> new", pure = true)
    public static RecipeChoice createPredicateChoice(final Predicate<? super ItemStack> stackPredicate) {
        return new PredicateRecipeChoice(stackPredicate);
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    /**
     * Gets the resulting itemstack after the brew has finished.
     *
     * @return the result itemstack
     */
    public ItemStack getResult() {
        return this.result.clone();
    }

    /**
     * Gets the input for the bottom 3 slots in the brewing stand.
     *
     * @return the bottom 3 slot ingredients
     */
    public RecipeChoice getInput() {
        return this.input.clone();
    }

    /**
     * Gets the ingredient in the top slot of the brewing stand.
     *
     * @return the top slot input
     */
    public RecipeChoice getIngredient() {
        return this.ingredient.clone();
    }

    @Override
    public String toString() {
        return "PotionMix{" +
            "result=" + this.result +
            ", base=" + this.input +
            ", addition=" + this.ingredient +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final PotionMix potionMix = (PotionMix) o;
        return this.key.equals(potionMix.key) && this.result.equals(potionMix.result) && this.input.equals(potionMix.input) && this.ingredient.equals(potionMix.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.result, this.input, this.ingredient);
    }
}
