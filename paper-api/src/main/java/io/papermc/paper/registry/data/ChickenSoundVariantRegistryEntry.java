package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import static io.papermc.paper.registry.data.RegistryBuilderProvider.transientHolder;

/**
 * A data-centric version-specific registry entry for the {@link Chicken.SoundVariant} type.
 */
@ApiStatus.NonExtendable
public interface ChickenSoundVariantRegistryEntry {

    /**
     * {@return the sounds played by adult chickens of this variant}
     */
    SoundSet adultSounds();

    /**
     * {@return the sounds played by baby chickens of this variant}
     */
    SoundSet babySounds();

    /**
     * The set of sounds played by a chicken of a specific age.
     */
    @ApiStatus.NonExtendable
    interface SoundSet {

        /**
         * Creates a new sound set based on the passed values.
         *
         * @param ambientSound the ambient sound, as returned by {@link #ambientSound()}
         * @param hurtSound    the hurt sound, as returned by {@link #hurtSound()}
         * @param deathSound   the death sound, as returned by {@link #deathSound()}
         * @param stepSound    the step sound, as returned by {@link #stepSound()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _ -> new", pure = true)
        static SoundSet of(
            final TypedKey<Sound> ambientSound,
            final TypedKey<Sound> hurtSound,
            final TypedKey<Sound> deathSound,
            final TypedKey<Sound> stepSound
        ) {
            return of(
                transientHolder(ambientSound),
                transientHolder(hurtSound),
                transientHolder(deathSound),
                transientHolder(stepSound)
            );
        }

        /**
         * Creates a new sound set based on the passed values.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param ambientSound the ambient sound, as returned by {@link #ambientSound()}
         * @param hurtSound    the hurt sound, as returned by {@link #hurtSound()}
         * @param deathSound   the death sound, as returned by {@link #deathSound()}
         * @param stepSound    the step sound, as returned by {@link #stepSound()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _ -> new", pure = true)
        static SoundSet of(
            final RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> deathSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> stepSound
        ) {
            record Impl(
                RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> deathSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> stepSound
            ) implements SoundSet {
            }

            return new Impl(ambientSound, hurtSound, deathSound, stepSound);
        }

        /**
         * {@return the sound played randomly while the chicken is idle}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound();

        /**
         * {@return the sound played when the chicken is hurt}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound();

        /**
         * {@return the sound played when the chicken dies}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> deathSound();

        /**
         * {@return the sound played when the chicken walks}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> stepSound();
    }

    /**
     * A mutable builder for the {@link ChickenSoundVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #adultSounds(SoundSet)}</li>
     *     <li>{@link #babySounds(SoundSet)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends ChickenSoundVariantRegistryEntry, RegistryBuilder<Chicken.SoundVariant> {

        /**
         * Sets the sounds played by adult chickens of this variant.
         *
         * @param adultSounds the sound set
         * @return this builder instance
         * @see ChickenSoundVariantRegistryEntry#adultSounds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder adultSounds(SoundSet adultSounds);

        /**
         * Sets the sounds played by baby chickens of this variant.
         *
         * @param babySounds the sound set
         * @return this builder instance
         * @see ChickenSoundVariantRegistryEntry#babySounds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder babySounds(SoundSet babySounds);
    }
}
