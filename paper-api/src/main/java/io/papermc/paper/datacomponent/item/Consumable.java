package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the properties for this item for when it is consumed.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CONSUMABLE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Consumable extends BuildableDataComponent<Consumable, Consumable.Builder> {

    @Contract(value = "-> new", pure = true)
    static Consumable.Builder consumable() {
        return ItemComponentTypesBridge.bridge().consumable();
    }

    @Contract(pure = true)
    @NonNegative float consumeSeconds();

    @Contract(pure = true)
    ItemUseAnimation animation();

    @Contract(pure = true)
    Key sound();

    @Contract(pure = true)
    boolean hasConsumeParticles();

    @Contract(pure = true)
    @Unmodifiable List<ConsumeEffect> consumeEffects();

    /**
     * Builder for {@link Consumable}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<Consumable> {

        /**
         * Sets the amount of time (in seconds) it takes to consume the item.
         *
         * @param consumeSeconds the consumption duration in seconds
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder consumeSeconds(@NonNegative float consumeSeconds);

        /**
         * Sets the hand animation used when a player consumes the item.
         *
         * @param animation the {@link ItemUseAnimation} representing the hand animation to be used
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder animation(ItemUseAnimation animation);

        /**
         * Sets the sound played when consuming the item.
         *
         * @param sound the {@link Key} representing the sound to be used
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(Key sound);

        /**
         * Sets whether consuming the item results in particle effects.
         *
         * @param hasConsumeParticles true to enable particle effects upon consumption, false to disable
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder hasConsumeParticles(boolean hasConsumeParticles);

        /**
         * Sets the effects that occur when an item is consumed.
         * <br>
         * <b>Note:</b> this clears any previous effects set.
         *
         * @param effects a list of {@link ConsumeEffect} instances representing the effects to apply upon consumption
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder effects(List<ConsumeEffect> effects);

        /**
         * Adds a single {@link ConsumeEffect} to the consumable item being built.
         *
         * @param effect the {@link ConsumeEffect} instance to add
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffect(ConsumeEffect effect);

        /**
         * Adds multiple {@link ConsumeEffect} instances to the consumable item being built.
         *
         * @param effects a list of {@link ConsumeEffect} instances to add
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffects(List<ConsumeEffect> effects);
    }
}
