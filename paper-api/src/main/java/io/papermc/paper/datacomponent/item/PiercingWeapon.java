package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface PiercingWeapon {

    /**
     * Returns a new builder for creating a Piercing Weapon.
     *
     * @return a builder instance
     */
    static Builder piercingWeapon() {
        return ItemComponentTypesBridge.bridge().piercingWeapon();
    }

    @NonNegative float minReach();

    @NonNegative float maxReach();

    @NonNegative float hitboxMargin();

    boolean dealsKnockback();

    boolean dismounts();

    @Nullable Key sound();

    @Nullable Key hitSound();

    /**
     * Builder for {@link PiercingWeapon}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<PiercingWeapon> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder minReach(@NonNegative float minReach);

        @Contract(value = "_ -> this", mutates = "this")
        Builder maxReach(@NonNegative float maxReach);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hitboxMargin(@NonNegative float hitboxMargin);

        @Contract(value = "_ -> this", mutates = "this")
        Builder dealsKnockback(boolean dealsKnockback);

        @Contract(value = "_ -> this", mutates = "this")
        Builder dismounts(boolean dismounts);

        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hitSound(@Nullable Key sound);
    }
}
