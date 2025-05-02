package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the contents of a potion (Potion, Splash Potion, Lingering Potion), or potion applied to a Tipped Arrow.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#POTION_CONTENTS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface PotionContents {

    @Contract(value = "-> new", pure = true)
    static PotionContents.Builder potionContents() {
        return ItemComponentTypesBridge.bridge().potionContents();
    }

    /**
     * The potion type in this item: the item will inherit all effects from this.
     *
     * @return potion type, or {@code null} if not present
     */
    @Contract(pure = true)
    @Nullable PotionType potion();

    /**
     * Overrides the visual color of the potion.
     *
     * @return color override, or {@code null} if not present
     * @apiNote alpha channel of the color is only relevant
     * for Tipped Arrow
     */
    @Contract(pure = true)
    @Nullable Color customColor();

    /**
     * Additional list of effect instances that this item should apply.
     *
     * @return effects
     */
    @Contract(pure = true)
    @Unmodifiable List<PotionEffect> customEffects();

    /**
     * Suffix to the translation key of the potion item.
     *
     * @return translation key suffix, or {@code null} if not present
     * @apiNote This is used in the display of tipped arrow and potion items.
     */
    @Contract(pure = true)
    @Nullable String customName();

    /**
     * All effects that this component applies.
     * <p>
     * This is a combination of the base potion type and any custom effects.
     *
     * @return an unmodifiable list of all effects.
     */
    @Contract(pure = true)
    @Unmodifiable List<PotionEffect> allEffects();

    /**
     * Computes the effective colour of this potion contents component.
     * <p>
     * This blends all custom effects, or uses a default fallback colour.
     * It may or may not have an alpha channel, used for tipped arrows.
     *
     * @return the effective colour this component would display with.
     */
    @Contract(pure = true)
    Color computeEffectiveColor();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<PotionContents> {

        /**
         * Sets the potion type for this builder.
         *
         * @param type builder
         * @return the builder for chaining
         * @see #potion()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder potion(@Nullable PotionType type);

        /**
         * Sets the color override for this builder.
         *
         * @param color color
         * @return the builder for chaining
         * @apiNote alpha channel of the color is supported only for Tipped Arrow
         * @see #customColor()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder customColor(@Nullable Color color);

        /**
         * Sets the suffix to the translation key of the potion item.
         *
         * @param name name
         * @return the builder for chaining
         * @see #customName()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder customName(@Nullable String name);

        /**
         * Adds a custom effect instance to this builder.
         *
         * @param effect effect
         * @return the builder for chaining
         * @see #customEffects()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addCustomEffect(PotionEffect effect);

        /**
         * Adds custom effect instances to this builder.
         *
         * @param effects effects
         * @return the builder for chaining
         * @see #customEffects()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addCustomEffects(List<PotionEffect> effects);
    }
}
