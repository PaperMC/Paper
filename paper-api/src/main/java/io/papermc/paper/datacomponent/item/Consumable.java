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

        @Contract(value = "_ -> this", mutates = "this")
        Builder consumeSeconds(@NonNegative float consumeSeconds);

        @Contract(value = "_ -> this", mutates = "this")
        Builder animation(ItemUseAnimation animation);

        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hasConsumeParticles(boolean hasConsumeParticles);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffect(ConsumeEffect effect);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffects(List<ConsumeEffect> effects);
    }
}
