package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import org.bukkit.Sound;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import static io.papermc.paper.registry.data.RegistryBuilderProvider.transientHolder;

/**
 * A data-centric version-specific registry entry for the {@link Cat.SoundVariant} type.
 */
@ApiStatus.NonExtendable
public interface CatSoundVariantRegistryEntry {

    /**
     * {@return the sounds played by adult cats of this variant}
     */
    SoundSet adultSounds();

    /**
     * {@return the sounds played by baby cats of this variant}
     */
    SoundSet babySounds();

    /**
     * The set of sounds played by a cat of a specific age.
     */
    @ApiStatus.NonExtendable
    interface SoundSet {

        /**
         * Creates a new sound set based on the passed values.
         *
         * @param ambientSound      the ambient sound, as returned by {@link #ambientSound()}
         * @param strayAmbientSound the stray ambient sound, as returned by {@link #strayAmbientSound()}
         * @param hissSound         the hiss sound, as returned by {@link #hissSound()}
         * @param hurtSound         the hurt sound, as returned by {@link #hurtSound()}
         * @param deathSound        the death sound, as returned by {@link #deathSound()}
         * @param eatSound          the eat sound, as returned by {@link #eatSound()}
         * @param begForFoodSound   the beg for food sound, as returned by {@link #begForFoodSound()}
         * @param purrSound         the purr sound, as returned by {@link #purrSound()}
         * @param purreowSound      the purreow sound, as returned by {@link #purreowSound()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _, _, _, _, _, _ -> new", pure = true)
        static SoundSet of(
            final TypedKey<Sound> ambientSound,
            final TypedKey<Sound> strayAmbientSound,
            final TypedKey<Sound> hissSound,
            final TypedKey<Sound> hurtSound,
            final TypedKey<Sound> deathSound,
            final TypedKey<Sound> eatSound,
            final TypedKey<Sound> begForFoodSound,
            final TypedKey<Sound> purrSound,
            final TypedKey<Sound> purreowSound
        ) {
            return of(
                transientHolder(ambientSound),
                transientHolder(strayAmbientSound),
                transientHolder(hissSound),
                transientHolder(hurtSound),
                transientHolder(deathSound),
                transientHolder(eatSound),
                transientHolder(begForFoodSound),
                transientHolder(purrSound),
                transientHolder(purreowSound)
            );
        }

        /**
         * Creates a new sound set based on the passed values.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param ambientSound      the ambient sound, as returned by {@link #ambientSound()}
         * @param strayAmbientSound the stray ambient sound, as returned by {@link #strayAmbientSound()}
         * @param hissSound         the hiss sound, as returned by {@link #hissSound()}
         * @param hurtSound         the hurt sound, as returned by {@link #hurtSound()}
         * @param deathSound        the death sound, as returned by {@link #deathSound()}
         * @param eatSound          the eat sound, as returned by {@link #eatSound()}
         * @param begForFoodSound   the beg for food sound, as returned by {@link #begForFoodSound()}
         * @param purrSound         the purr sound, as returned by {@link #purrSound()}
         * @param purreowSound      the purreow sound, as returned by {@link #purreowSound()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _, _, _, _, _, _ -> new", pure = true)
        static SoundSet of(
            final RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> strayAmbientSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> hissSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> deathSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> eatSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> begForFoodSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> purrSound,
            final RegistryHolder<Sound, SoundEventRegistryEntry> purreowSound
        ) {
            record Impl(
                RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> strayAmbientSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> hissSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> deathSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> eatSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> begForFoodSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> purrSound,
                RegistryHolder<Sound, SoundEventRegistryEntry> purreowSound
            ) implements SoundSet {
            }

            return new Impl(ambientSound, strayAmbientSound, hissSound, hurtSound, deathSound, eatSound, begForFoodSound, purrSound, purreowSound);
        }

        /**
         * {@return the sound played randomly while the cat is tamed and idle, unless the purreow sound is played instead}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound();

        /**
         * {@return the sound played randomly while the cat is untamed and idle}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> strayAmbientSound();

        /**
         * {@return the sound played when the cat hisses}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> hissSound();

        /**
         * {@return the sound played when the cat is hurt}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound();

        /**
         * {@return the sound played when the cat dies}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> deathSound();

        /**
         * {@return the sound played when the cat eats}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> eatSound();

        /**
         * {@return the sound played while the cat is untamed and tempted by food}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> begForFoodSound();

        /**
         * {@return the sound played while the cat is lying down, or instead of the ambient sound while it's tamed and in love}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> purrSound();

        /**
         * {@return the sound sometimes played instead of the ambient sound while the cat is tamed and idle}
         */
        RegistryHolder<Sound, SoundEventRegistryEntry> purreowSound();
    }

    /**
     * A mutable builder for the {@link CatSoundVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #adultSounds(SoundSet)}</li>
     *     <li>{@link #babySounds(SoundSet)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends CatSoundVariantRegistryEntry, RegistryBuilder<Cat.SoundVariant> {

        /**
         * Sets the sounds played by adult cats of this variant.
         *
         * @param adultSounds the sound set
         * @return this builder instance
         * @see CatSoundVariantRegistryEntry#adultSounds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder adultSounds(SoundSet adultSounds);

        /**
         * Sets the sounds played by baby cats of this variant.
         *
         * @param babySounds the sound set
         * @return this builder instance
         * @see CatSoundVariantRegistryEntry#babySounds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder babySounds(SoundSet babySounds);
    }
}
