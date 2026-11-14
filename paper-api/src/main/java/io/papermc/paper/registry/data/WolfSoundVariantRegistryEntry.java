package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import org.bukkit.Sound;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import static io.papermc.paper.registry.data.RegistryBuilderProvider.transientHolder;

/**
 * A data-centric version-specific registry entry for the {@link Wolf.SoundVariant} type.
 */
@ApiStatus.NonExtendable
public interface WolfSoundVariantRegistryEntry {

    /**
     * {@return the sounds played by adult wolves of this variant}
     */
    SoundSet adultSounds();

    /**
     * {@return the sounds played by baby wolves of this variant}
     */
    SoundSet babySounds();

    /**
     * The set of sounds played by a wolf of a specific age.
     */
    @ApiStatus.NonExtendable
    interface SoundSet {

        /**
         * Creates a new sound set based on the passed values.
         *
         * @param ambientSound the ambient sound, as returned by {@link #ambientSound()}
         * @param deathSound   the death sound, as returned by {@link #deathSound()}
         * @param growlSound   the growl sound, as returned by {@link #growlSound()}
         * @param hurtSound    the hurt sound, as returned by {@link #hurtSound()}
         * @param pantSound    the pant sound, as returned by {@link #pantSound()}
         * @param whineSound   the whine sound, as returned by {@link #whineSound()}
         * @param stepSound    the step sound, as returned by {@link #stepSound()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _, _, _, _ -> new", pure = true)
        static SoundSet of(
            final TypedKey<Sound> ambientSound,
            final TypedKey<Sound> deathSound,
            final TypedKey<Sound> growlSound,
            final TypedKey<Sound> hurtSound,
            final TypedKey<Sound> pantSound,
            final TypedKey<Sound> whineSound,
            final TypedKey<Sound> stepSound
        ) {
            return of(
                transientHolder(ambientSound),
                transientHolder(deathSound),
                transientHolder(growlSound),
                transientHolder(hurtSound),
                transientHolder(pantSound),
                transientHolder(whineSound),
                transientHolder(stepSound)
            );
        }

        /**
         * Creates a new sound set based on the passed values.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param ambientSound the ambient sound, as returned by {@link #ambientSound()}
         * @param deathSound   the death sound, as returned by {@link #deathSound()}
         * @param growlSound   the growl sound, as returned by {@link #growlSound()}
         * @param hurtSound    the hurt sound, as returned by {@link #hurtSound()}
         * @param pantSound    the pant sound, as returned by {@link #pantSound()}
         * @param whineSound   the whine sound, as returned by {@link #whineSound()}
         * @param stepSound    the step sound, as returned by {@link #stepSound()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _, _, _, _ -> new", pure = true)
        static SoundSet of(
            final RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> deathSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> growlSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> pantSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> whineSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> stepSound
        ) {
            record Impl(
                RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> deathSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> growlSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> pantSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> whineSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> stepSound
            ) implements SoundSet {
            }

            return new Impl(ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound, stepSound);
        }

        /**
         * {@return the sound played randomly while the wolf is idle}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound();

        /**
         * {@return the sound played when the wolf dies}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> deathSound();

        /**
         * {@return the sound played instead of the ambient sound while the wolf is angry}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> growlSound();

        /**
         * {@return the sound played when the wolf is hurt}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound();

        /**
         * {@return the sound played randomly while the wolf is idle, unless the whine sound is played instead}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> pantSound();

        /**
         * {@return the sound played randomly while the wolf is idle, tamed and not at full health}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> whineSound();

        /**
         * {@return the sound played when the wolf walks}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> stepSound();
    }

    /**
     * A mutable builder for the {@link WolfSoundVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #adultSounds(SoundSet)}</li>
     *     <li>{@link #babySounds(SoundSet)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends WolfSoundVariantRegistryEntry, RegistryBuilder<Wolf.SoundVariant> {

        /**
         * Sets the sounds played by adult wolves of this variant.
         *
         * @param adultSounds the sound set
         * @return this builder instance
         * @see WolfSoundVariantRegistryEntry#adultSounds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder adultSounds(SoundSet adultSounds);

        /**
         * Sets the sounds played by baby wolves of this variant.
         *
         * @param babySounds the sound set
         * @return this builder instance
         * @see WolfSoundVariantRegistryEntry#babySounds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder babySounds(SoundSet babySounds);
    }
}
