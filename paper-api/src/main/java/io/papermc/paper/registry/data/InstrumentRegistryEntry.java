package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.MusicInstrument;
import org.bukkit.Sound;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import java.util.function.Consumer;

/**
 * A data-centric version-specific registry entry for the {@link org.bukkit.MusicInstrument} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface InstrumentRegistryEntry {

    /**
     * Provides the sound event of the instrument.
     *
     * @return the sound event.
     * @see MusicInstrument#getSound()
     */
    @Contract(pure = true)
    RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent();

    /**
     * Provides the duration of the instrument, which is time to use.
     *
     * @return the duration.
     * @see MusicInstrument#getDuration()
     */
    @Contract(pure = true)
    @Positive float duration();

    /**
     * Provides the range of the instrument, which is range of the sound.
     *
     * @return the range.
     * @see MusicInstrument#getRange()
     */
    @Contract(pure = true)
    @Positive float range();

    /**
     * Provides the description of the instrument, which is used in the item tooltip.
     *
     * @return the description.
     * @see MusicInstrument#description()
     */
    @Contract(pure = true)
    Component description();

    /**
     * A mutable builder for the {@link InstrumentRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>
     *         {@link #soundEvent(TypedKey)}, {@link #soundEvent(Consumer)} or {@link #soundEvent(RegistryHolder)}
     *     </li>
     *     <li>{@link #duration(float)}</li>
     *     <li>{@link #range(float)}</li>
     *     <li>{@link #description(Component)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends InstrumentRegistryEntry, RegistryBuilder<MusicInstrument> {

        /**
         * Sets the sound event for this instrument to a sound event present
         * in the {@link io.papermc.paper.registry.RegistryKey#SOUND_EVENT} registry.
         * <p>This will override both {@link #soundEvent(Consumer)} and {@link #soundEvent(RegistryHolder)}</p>
         *
         * @param soundEvent the sound event
         * @return this builder
         * @see #soundEvent(Consumer)
         * @see InstrumentRegistryEntry#soundEvent()
         * @see MusicInstrument#getSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(TypedKey<Sound> soundEvent);

        /**
         * Sets the sound event for this instrument to a new sound event.
         * <p>This will override both {@link #soundEvent(TypedKey)} and {@link #soundEvent(RegistryHolder)}</p>
         *
         * @param soundEvent the sound event
         * @return this builder
         * @see #soundEvent(TypedKey)
         * @see InstrumentRegistryEntry#soundEvent()
         * @see MusicInstrument#getSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(Consumer<RegistryBuilderFactory<Sound, ? extends SoundEventRegistryEntry.Builder>> soundEvent);

        /**
         * Sets the sound event for this instrument.
         * <p>This will override both {@link #soundEvent(Consumer)} and {@link #soundEvent(TypedKey)}</p>
         *
         * @param soundEvent the sound event
         * @return this builder
         * @see #soundEvent(TypedKey)
         * @see #soundEvent(Consumer)
         * @see InstrumentRegistryEntry#soundEvent()
         * @see MusicInstrument#getSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent);

        /**
         * Sets the duration of use for this instrument.
         *
         * @param duration the duration (positive)
         * @return this builder
         * @see InstrumentRegistryEntry#duration()
         * @see MusicInstrument#getDuration()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder duration(@Positive float duration);

        /**
         * Sets the range for this instrument.
         *
         * @param range the range (positive)
         * @return this builder
         * @see InstrumentRegistryEntry#range()
         * @see MusicInstrument#getRange()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder range(@Positive float range);

        /**
         * Sets the description for this instrument.
         *
         * @param description the description
         * @return this builder
         * @see InstrumentRegistryEntry#description()
         * @see MusicInstrument#description()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);
    }
}
