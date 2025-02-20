package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.MusicInstrument;
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
     * Provides the duration of the instrument, which is time to use.
     *
     * @return the duration.
     */
    @Contract(pure = true)
    @Positive float duration();

    /**
     * Provides the range of the instrument, which is range of the sound.
     *
     * @return the range.
     */
    @Contract(pure = true)
    @Positive float range();

    /**
     * Provides the description of the instrument, which is used in the item tooltip.
     *
     * @return the description.
     */
    Component description();

    /**
     * Provides the sound event of the instrument.
     *
     * @return the sound event.
     */
    @Contract(pure = true)
    Either<TypedKey<Sound>, SoundEventRegistryEntry> soundEvent();

    /**
     * A mutable builder for the {@link InstrumentRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #duration(float)}</li>
     *     <li>{@link #range(float)}</li>
     *     <li>{@link #description(Component)}</li>
     *     <li>
     *         {@link #soundEvent(TypedKey)} or {@link #soundEvent(Consumer)}
     *     </li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends InstrumentRegistryEntry, RegistryBuilder<MusicInstrument> {

        /**
         * Sets the duration of use for this instrument.
         *
         * @param duration the duration (positive)
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder duration(@Positive float duration);

        /**
         * Sets the range for this instrument.
         *
         * @param range the range (positive)
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder range(@Positive float range);

        /**
         * Sets the description for this instrument.
         *
         * @param description the description
         * @return this builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        /**
         * Sets the sound event for this instrument to a sound event present
         * in the {@link io.papermc.paper.registry.RegistryKey#SOUND_EVENT} registry.
         * <p>This will override {@link #soundEvent(Consumer)}</p>
         *
         * @param soundEvent the sound event
         * @return this builder
         * @see #soundEvent(Consumer)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(TypedKey<Sound> soundEvent);

        /**
         * Sets the sound event for this instrument to a new sound event.
         * <p>This will override {@link #soundEvent(TypedKey)}</p>
         *
         * @param soundEvent the sound event
         * @return this builder
         * @see #soundEvent(TypedKey)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundEvent(Consumer<RegistryBuilderFactory<Sound, ? extends SoundEventRegistryEntry.Builder>> soundEvent);
    }

}
