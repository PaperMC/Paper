package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Sets whether this item should protect the entity upon death, and what effects should be played.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#DEATH_PROTECTION
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DeathProtection {

    @Contract(value = "_ -> new", pure = true)
    static DeathProtection deathProtection(final List<ConsumeEffect> deathEffects) {
        return deathProtection().addEffects(deathEffects).build();
    }

    @Contract(value = "-> new", pure = true)
    static DeathProtection.Builder deathProtection() {
        return ItemComponentTypesBridge.bridge().deathProtection();
    }

    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ConsumeEffect> deathEffects();

    /**
     * Builder for {@link DeathProtection}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<DeathProtection> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffect(ConsumeEffect effect);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addEffects(List<ConsumeEffect> effects);
    }
}
