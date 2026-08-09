package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
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

    boolean dealsKnockback();

    boolean dismounts();

    @Nullable Sound sound();

    @Nullable Sound hitSound();

    /**
     * Builder for {@link PiercingWeapon}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<PiercingWeapon> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder dealsKnockback(boolean dealsKnockback);

        @Contract(value = "_ -> this", mutates = "this")
        Builder dismounts(boolean dismounts);

        /**
         * Sets the sound to play while this weapon is active.
         *
         * @param sound the sound, or null to play no sound
         * @return the builder for chaining
         * @see Sound#create(net.kyori.adventure.key.Key, Float)
         * @see Sound#create(java.util.function.Consumer)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(@Nullable Sound sound);

        /**
         * Sets the sound to play when this weapon hits an entity.
         *
         * @param sound the sound, or null to play no sound
         * @return the builder for chaining
         * @see Sound#create(net.kyori.adventure.key.Key, Float)
         * @see Sound#create(java.util.function.Consumer)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder hitSound(@Nullable Sound sound);
    }
}
