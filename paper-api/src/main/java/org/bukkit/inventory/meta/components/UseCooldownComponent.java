package org.bukkit.inventory.meta.components;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a component which determines the cooldown applied when using this item before it is available for use again.
 */
@ApiStatus.Experimental
public interface UseCooldownComponent extends ConfigurationSerializable {

    /**
     * Gets the time in seconds it will take for an item in this cooldown group
     * to be available to use again.
     *
     * @return cooldown time
     */
    float getCooldownSeconds();

    /**
     * Sets the time in seconds it will take for an item in this cooldown group
     * to be available to use again.
     *
     * @param cooldown new eat time, must be greater than 0
     */
    void setCooldownSeconds(float cooldown);

    /**
     * Gets the custom cooldown group to be used for similar items, if set.
     *
     * @return the cooldown group
     */
    @Nullable
    NamespacedKey getCooldownGroup();

    /**
     * Sets the custom cooldown group to be used for similar items.
     *
     * @param group the cooldown group
     */
    void setCooldownGroup(@Nullable NamespacedKey group);
}
