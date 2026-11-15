package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.holder.RegistryHolder;
import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link Cow.SoundVariant} type.
 */
@ApiStatus.NonExtendable
public interface CowSoundVariantRegistryEntry {

    /**
     * {@return the sound played randomly while the cow is idle}
     */
    RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound();

    /**
     * {@return the sound played when the cow is hurt}
     */
    RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound();

    /**
     * {@return the sound played when the cow dies}
     */
    RegistryHolder<Sound, SoundEventRegistryEntry> deathSound();

    /**
     * {@return the sound played when the cow walks}
     */
    RegistryHolder<Sound, SoundEventRegistryEntry> stepSound();

    /**
     * A mutable builder for the {@link CowSoundVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #ambientSound(TypedKey)} or {@link #ambientSound(RegistryHolder)}</li>
     *     <li>{@link #hurtSound(TypedKey)} or {@link #hurtSound(RegistryHolder)}</li>
     *     <li>{@link #deathSound(TypedKey)} or {@link #deathSound(RegistryHolder)}</li>
     *     <li>{@link #stepSound(TypedKey)} or {@link #stepSound(RegistryHolder)}</li>
     * </ul>
     */
    @ApiStatus.NonExtendable
    interface Builder extends CowSoundVariantRegistryEntry, RegistryBuilder<Cow.SoundVariant> {

        /**
         * Sets the ambient sound.
         *
         * @param ambientSound the key of the ambient sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#ambientSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder ambientSound(TypedKey<Sound> ambientSound);

        /**
         * Sets the ambient sound.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param ambientSound the ambient sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#ambientSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder ambientSound(RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound);

        /**
         * Sets the hurt sound.
         *
         * @param hurtSound the key of the hurt sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#hurtSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder hurtSound(TypedKey<Sound> hurtSound);

        /**
         * Sets the hurt sound.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param hurtSound the hurt sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#hurtSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder hurtSound(RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound);

        /**
         * Sets the death sound.
         *
         * @param deathSound the key of the death sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#deathSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder deathSound(TypedKey<Sound> deathSound);

        /**
         * Sets the death sound.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param deathSound the death sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#deathSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder deathSound(RegistryHolder<Sound, SoundEventRegistryEntry> deathSound);

        /**
         * Sets the step sound.
         *
         * @param stepSound the key of the step sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#stepSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder stepSound(TypedKey<Sound> stepSound);

        /**
         * Sets the step sound.
         * <p>
         * {@link RegistryHolder} instances can be created with {@link io.papermc.paper.registry.event.RegistryFactory}.
         *
         * @param stepSound the step sound
         * @return this builder instance
         * @see CowSoundVariantRegistryEntry#stepSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder stepSound(RegistryHolder<Sound, SoundEventRegistryEntry> stepSound);
    }
}
