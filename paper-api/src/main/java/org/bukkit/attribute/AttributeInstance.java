package org.bukkit.attribute;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mutable instance of an attribute and its associated modifiers
 * and values.
 */
public interface AttributeInstance {

    /**
     * The attribute pertaining to this instance.
     *
     * @return the attribute
     */
    @NotNull
    Attribute getAttribute();

    /**
     * Base value of this instance before modifiers are applied.
     *
     * @return base value
     */
    double getBaseValue();

    /**
     * Set the base value of this instance.
     *
     * @param value new base value
     */
    void setBaseValue(double value);

    /**
     * Get all modifiers present on this instance.
     *
     * @return a copied collection of all modifiers
     */
    @NotNull
    Collection<AttributeModifier> getModifiers();

    // Paper start
    /**
     * Gets the modifier with the corresponding key.
     *
     * @param key the key of the modifier
     * @return the modifier, if it exists
     */
    @org.jetbrains.annotations.Nullable AttributeModifier getModifier(@NotNull net.kyori.adventure.key.Key key);

    /**
     * Remove a modifier with the corresponding key from this instance.
     *
     * @param key the key of the modifier
     */
    void removeModifier(@NotNull net.kyori.adventure.key.Key key);

    /**
     * Gets the modifier with the corresponding UUID.
     *
     * @param uuid the UUID of the modifier
     * @return the modifier, if it exists
     * @deprecated use {@link #getModifier(net.kyori.adventure.key.Key)}, modifiers are no longer stored by UUID
     */
    @Deprecated(forRemoval = true, since = "1.21")
    @org.jetbrains.annotations.Nullable AttributeModifier getModifier(@NotNull java.util.UUID uuid);

    /**
     * Remove a modifier with the corresponding UUID from this instance.
     *
     * @param uuid the UUID of the modifier
     * @deprecated use {@link #removeModifier(net.kyori.adventure.key.Key)}, modifiers are no longer stored by UUID
     */
    @Deprecated(forRemoval = true, since = "1.21")
    void removeModifier(@NotNull java.util.UUID uuid);
    // Paper end

    /**
     * Add a modifier to this instance.
     *
     * @param modifier to add
     */
    void addModifier(@NotNull AttributeModifier modifier);

    // Paper start - Transient modifier API
    /**
     * Add a transient modifier to this instance.
     * Transient modifiers are not persisted (saved with the NBT data)
     *
     * @param modifier to add
     */
    void addTransientModifier(@NotNull AttributeModifier modifier);
    // Paper end

    /**
     * Remove a modifier from this instance.
     *
     * @param modifier to remove
     */
    void removeModifier(@NotNull AttributeModifier modifier);

    /**
     * Get the value of this instance after all associated modifiers have been
     * applied.
     *
     * @return the total attribute value
     */
    double getValue();

    /**
     * Gets the default value of the Attribute attached to this instance.
     *
     * @return server default value
     */
    double getDefaultValue();
}
