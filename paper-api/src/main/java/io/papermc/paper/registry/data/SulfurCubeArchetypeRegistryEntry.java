package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.SulfurCube;
import org.bukkit.inventory.ItemType;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;
import static io.papermc.paper.util.BoundChecker.requirePositive;

/**
 * A data-centric version-specific registry entry for the {@link SulfurCube.Archetype} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface SulfurCubeArchetypeRegistryEntry {

    /**
     * An attribute entry to apply to a sulfur cube of this archetype.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface AttributeEntry {

        /**
         * {@return the attribute to modify}
         */
        TypedKey<Attribute> attribute();

        /**
         * {@return the modifier of the given attribute}
         */
        AttributeModifier modifier();

        /**
         * Creates a new attribute entry instance based on the passed values.
         *
         * @param attribute the attribute to modify, as returned by {@link #attribute()}
         * @param modifier the modifier of the given attribute, as returned by {@link #modifier()}
         * @return the created instance
         */
        @Contract(value = "_, _ -> new", pure = true)
        static AttributeEntry of(final TypedKey<Attribute> attribute, final AttributeModifier modifier) {
            record Impl(TypedKey<Attribute> attribute, AttributeModifier modifier) implements AttributeEntry {
            }

            return new Impl(attribute, modifier);
        }
    }

    /**
     * The contact damage a sulfur cube of this archetype will deal when pushed
     * by another entity.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface ContactDamage {

        /**
         * {@return the damage type of the damage dealt}
         */
        TypedKey<DamageType> damageType();

        /**
         * {@return the damage amount of the damage dealt}
         * @apiNote for non-constant damage this will return one possible sample
         * instead of the exact amount
         */
        @NonNegative float amount(); // todo expose FloatProvider/IntProvider in a consistent way (should match Machine's PR for dimension type)

        /**
         * {@return whether the sulfur cube of this damage should be recorded in the
         * damage source}
         */
        boolean attributeToSource();

        /**
         * Creates a new contact damage instance based on the passed values.
         *
         * @param damageType the damage type of the damage dealt, as returned by {@link #damageType()}
         * @param amount the damage amount of the damage dealt, as returned by {@link #amount()}
         * @param attributeToSource whether the sulfur cube of this damage should be recorded, as returned by {@link #attributeToSource()}
         * @return the created instance
         */
        @Contract(value = "_, _, _ -> new", pure = true)
        static ContactDamage of(final TypedKey<DamageType> damageType, final @NonNegative float amount, final boolean attributeToSource) {
            record Impl(TypedKey<DamageType> damageType, @NonNegative float amount, boolean attributeToSource) implements ContactDamage {
            }

            return new Impl(damageType, requireNonNegative(amount, "amount"), attributeToSource);
        }
    }

    /**
     * The explosion settings of a sulfur cube of this archetype.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface ExplosionSettings {

        /**
         * {@return the radius of the explosion}
         */
        @NonNegative int power();

        /**
         * {@return whether the explosion will produce fire on the ground}
         */
        boolean incendiary();

        /**
         * {@return the amount of ticks needed once the sulfur cube
         * is ignited before exploding}
         */
        @Positive int fuseTicks();

        /**
         * Creates a new explosion settings instance based on the passed values.
         *
         * @param power the radius of the explosion, as returned by {@link #power()}
         * @param incendiary whether the explosion will produce fire, as returned by {@link #incendiary()}
         * @param fuseTicks the amount of ticks needed once the sulfur cube
         * is ignited before exploding, as returned by {@link #fuseTicks()}
         * @return the created instance
         */
        @Contract(value = "_, _, _ -> new", pure = true)
        static ExplosionSettings of(final @NonNegative int power, final boolean incendiary, final @Positive int fuseTicks) {
            record Impl(@NonNegative int power, boolean incendiary, @Positive int fuseTicks) implements ExplosionSettings {
            }

            return new Impl(requireNonNegative(power, "power"), incendiary, requirePositive(fuseTicks, "fuseTicks"));
        }
    }

    /**
     * The knockback modifiers a sulfur cube of this archetype will receive
     * when knocked by another entity.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface KnockbackModifiers {

        /**
         * {@return the horizontal base power of the knockback to receive before all other environmental/contextual changes}
         */
        float horizontalPower();

        /**
         * {@return the vertical base power of the knockback to receive before all other environmental/contextual changes}
         */
        float verticalPower();

        /**
         * Creates a new knockback modifiers instance based on the passed values.
         *
         * @param horizontalPower the horizontal base power of the knockback, as returned by {@link #horizontalPower()}
         * @param verticalPower the vertical base power of the knockback, as returned by {@link #verticalPower()}
         * @return the created instance
         */
        @Contract(value = "_, _ -> new", pure = true)
        static KnockbackModifiers of(final float horizontalPower, final float verticalPower) {
            record Impl(float horizontalPower, float verticalPower) implements KnockbackModifiers {
            }

            return new Impl(horizontalPower, verticalPower);
        }
    }

    /**
     * The sound settings of a sulfur cube of this archetype.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface SoundSettings {

        /**
         * {@return the sound played once the sulfur cube is knocked}
         */
        TypedKey<Sound> hitSound(); // todo should take a RegistryHolder but need more thoughts on the create method

        /**
         * {@return the sound played once the sulfur cube is pushed}
         */
        TypedKey<Sound> pushSound();

        /**
         * {@return the smallest impulse required to play the push sound}
         */
        float pushSoundImpulseThreshold();

        /**
         * {@return the amount of seconds before the push sound can be played again}
         */
        float pushSoundCooldown();

        /**
         * Creates a new sound settings instance based on the passed values.
         *
         * @param hitSound the sound played once the sulfur cube is knocked, as returned by {@link #hitSound()}
         * @param pushSound the sound played once the sulfur cube is pushed, as returned by {@link #pushSound()}
         * @param pushSoundImpulseThreshold the smallest impulse required to play the push sound, as returned by {@link #pushSoundImpulseThreshold()}
         * @param pushSoundCooldown the amount of seconds before the push sound can be played again, as returned by {@link #pushSoundCooldown()}
         * @return the created instance
         */
        @Contract(value = "_, _, _, _ -> new", pure = true)
        static SoundSettings of(final TypedKey<Sound> hitSound, final TypedKey<Sound> pushSound, final float pushSoundImpulseThreshold, final float pushSoundCooldown) {
            record Impl(TypedKey<Sound> hitSound, TypedKey<Sound> pushSound, float pushSoundImpulseThreshold, float pushSoundCooldown) implements SoundSettings {
            }

            return new Impl(hitSound, pushSound, pushSoundImpulseThreshold, pushSoundCooldown);
        }
    }

    /**
     * Provides the items a sulfur cube of this archetype can absorb.
     *
     * @return the items absorbable
     */
    RegistryKeySet<ItemType> items();

    /**
     * Provides the attribute modifiers to apply to a sulfur cube of this archetype.
     *
     * @return the attribute modifiers to apply
     */
    List<AttributeEntry> attributeModifiers();

    /**
     * Checks if a sulfur cube of this archetype can float in liquid.
     *
     * @return the ability of the sulfur cube to float
     */
    boolean buoyant();

    /**
     * Provides the explosion settings of a sulfur cube of this archetype.
     * If defined the sulfur cube can be ignited like a tnt.
     *
     * @return the explosion settings
     */
    @Nullable ExplosionSettings explosion();

    /**
     * Provides the damage a sulfur cube of this archetype can deal
     * when in contact of another entity.
     *
     * @return the contact damage
     */
    @Nullable ContactDamage contactDamage();

    /**
     * Provides the knockback modifiers a sulfur cube of this archetype will receive
     * when knocked by another entity.
     *
     * @return the knockback modifiers
     */
    KnockbackModifiers knockbackModifiers();

    /**
     * Provides the sound settings of a sulfur cube of this archetype.
     *
     * @return the sound settings
     */
    SoundSettings soundSettings();

    /**
     * A mutable builder for the {@link SulfurCubeArchetypeRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #items(RegistryKeySet)}</li>
     *     <li>{@link #knockbackModifiers(KnockbackModifiers)}</li>
     *     <li>{@link #soundSettings(SoundSettings)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends SulfurCubeArchetypeRegistryEntry, RegistryBuilder<SulfurCube.Archetype> {

        /**
         * Sets the items a sulfur cube of this archetype can absorb.
         *
         * @param items the items absorbable
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#items()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder items(RegistryKeySet<ItemType> items);

        /**
         * Sets the attribute modifiers to apply to a sulfur cube of this archetype.
         *
         * @param entries the attribute modifiers to apply
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#attributeModifiers()
         */
        @Contract(value = "_ -> this", mutates = "this")
        default Builder attributeModifiers(final AttributeEntry... entries) {
            return this.attributeModifiers(List.of(entries));
        }

        /**
         * Sets the attribute modifiers to apply to a sulfur cube of this archetype.
         *
         * @param entries the attribute modifiers to apply
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#attributeModifiers()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder attributeModifiers(Iterable<AttributeEntry> entries);

        /**
         * Sets the ability of the sulfur cube to float.
         *
         * @param buoyant the ability of the sulfur cube to float
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#buoyant()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder buoyant(boolean buoyant);

        /**
         * Sets the explosion settings of a sulfur cube of this archetype.
         *
         * @param settings the explosion settings
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#explosion()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder explosion(@Nullable ExplosionSettings settings);

        /**
         * Sets the damage a sulfur cube of this archetype can deal
         * when in contact of another entity.
         *
         * @param damage the contact damage
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#contactDamage()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder contactDamage(@Nullable ContactDamage damage);

        /**
         * Sets the knockback modifiers a sulfur cube of this archetype will receive
         * when knocked by another entity.
         *
         * @param modifiers the knockback modifiers
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#knockbackModifiers()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder knockbackModifiers(KnockbackModifiers modifiers);

        /**
         * Sets the sound settings of a sulfur cube of this archetype.
         *
         * @param settings the sound settings
         * @return this builder instance
         * @see SulfurCubeArchetypeRegistryEntry#soundSettings()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder soundSettings(SoundSettings settings);
    }
}
