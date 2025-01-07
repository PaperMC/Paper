package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.MusicInstrument;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link org.bukkit.MusicInstrument} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface InstrumentRegistryEntry {

    @Contract(pure = true)
    @NonNegative
    float duration();

    @Contract(pure = true)
    @NonNegative
    float range();

    Component description();

    Sound sound();

    /**
     * A mutable builder for the {@link InstrumentRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #duration(float)}</li>
     *     <li>{@link #range(float)}</li>
     *     <li>{@link #description(Component)}</li>
     *     <li>{@link #sound(Sound)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends InstrumentRegistryEntry, RegistryBuilder<MusicInstrument> {

        /**
         *
         *
         * @param duration
         * @return this builder instance.
         * @see InstrumentRegistryEntry#duration()
         * @see MusicInstrument#getDuration()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder duration(@NonNegative float duration);

        /**
         *
         *
         * @param range
         * @return this builder instance.
         * @see InstrumentRegistryEntry#range()
         * @see MusicInstrument#getRange() ()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder range(@NonNegative float range);

        /**
         *
         *
         * @param description
         * @return this builder instance.
         * @see InstrumentRegistryEntry#description()
         * @see MusicInstrument#description()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        /**
         *
         *
         * @param sound
         * @return this builder instance.
         * @see InstrumentRegistryEntry#sound()
         * @see MusicInstrument#getSoundEvent()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(Sound sound);
    }

}
