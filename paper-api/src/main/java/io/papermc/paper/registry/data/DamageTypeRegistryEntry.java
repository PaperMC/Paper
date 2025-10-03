package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageScaling;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DeathMessageType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link DamageType} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface DamageTypeRegistryEntry {

    /**
     * Provides part of the death message translation key. (death.attack.&lt;message_id&gt;)
     * <p>
     * <strong>Note</strong> The translation key is only used if
     * {@link #deathMessageType()} is {@link DeathMessageType#DEFAULT}
     *
     * @return part of the translation key
     */
    String messageId();

    /**
     * Provides the {@link DamageScaling} for this damage type.
     *
     * @return the damage scaling
     */
    DamageScaling damageScaling();

    /**
     * Provides the amount of hunger exhaustion caused by this damage type.
     *
     * @return the exhaustion
     */
    float exhaustion();

    /**
     * Provides the {@link DamageEffect} for this damage type.
     *
     * @return the damage effect
     */
    DamageEffect damageEffect();

    /**
     * Provides the {@link DeathMessageType} for this damage type.
     *
     * @return the death message type
     */
    DeathMessageType deathMessageType();

    /**
     * A mutable builder for the {@link DamageTypeRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #messageId(String)}</li>
     *     <li>{@link #exhaustion(float)}</li>
     *     <li>{@link #damageScaling(DamageScaling)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DamageTypeRegistryEntry, RegistryBuilder<DamageType> {

        /**
         * Sets part of the death message translation key.
         *
         * @return this builder instance.
         * @see DamageTypeRegistryEntry#messageId()
         * @see DamageType#getTranslationKey()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder messageId(String messageId);

        /**
         * Sets the amount of hunger exhaustion caused by this damage type.
         *
         * @return this builder instance.
         * @see DamageTypeRegistryEntry#exhaustion()
         * @see DamageType#getExhaustion()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder exhaustion(float exhaustion);

        /**
         * Sets the {@link DamageScaling} for this damage type.
         *
         * @return this builder instance.
         * @see DamageTypeRegistryEntry#damageScaling()
         * @see DamageType#getDamageScaling()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder damageScaling(DamageScaling scaling);

        /**
         * Sets the {@link DamageEffect} for this damage type.
         *
         * @return this builder instance.
         * @see DamageTypeRegistryEntry#damageEffect()
         * @see DamageType#getDamageEffect()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder damageEffect(DamageEffect effect);

        /**
         * Sets the {@link DeathMessageType} for this damage type.
         *
         * @return this builder instance.
         * @see DamageTypeRegistryEntry#deathMessageType()
         * @see DamageType#getDeathMessageType()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder deathMessageType(DeathMessageType deathMessageType);
    }
}
