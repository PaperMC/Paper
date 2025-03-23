package org.bukkit.inventory.meta.components;

import java.util.Collection;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a component which can turn any item into equippable armor.
 */
@ApiStatus.Experimental
public interface EquippableComponent extends ConfigurationSerializable {

    /**
     * Gets the slot the item can be equipped to.
     *
     * @return slot
     */
    @NotNull
    EquipmentSlot getSlot();

    /**
     * Sets the slot the item can be equipped to.
     *
     * @param slot new slot
     */
    void setSlot(@NotNull EquipmentSlot slot);

    /**
     * Gets the sound to play when the item is equipped.
     *
     * @return the sound
     */
    @NotNull // Paper
    Sound getEquipSound();

    /**
     * Sets the sound to play when the item is equipped.
     *
     * @param sound sound or null for current default
     */
    void setEquipSound(@Nullable Sound sound);

    /**
     * Gets the key of the model to use when equipped.
     *
     * @return model key
     */
    @Nullable
    NamespacedKey getModel();

    /**
     * Sets the key of the model to use when equipped.
     *
     * @param key model key
     */
    void setModel(@Nullable NamespacedKey key);

    /**
     * Gets the key of the camera overlay to use when equipped.
     *
     * @return camera overlay key
     */
    @Nullable
    NamespacedKey getCameraOverlay();

    /**
     * Sets the key of the camera overlay to use when equipped.
     *
     * @param key camera overlay key
     */
    void setCameraOverlay(@Nullable NamespacedKey key);

    /**
     * Gets the entities which can equip this item.
     *
     * @return the entities
     */
    @Nullable
    Collection<EntityType> getAllowedEntities();

    /**
     * Sets the entities which can equip this item.
     *
     * @param entities the entity types
     */
    void setAllowedEntities(@Nullable EntityType entities);

    /**
     * Sets the entities which can equip this item.
     *
     * @param entities the entity types
     */
    void setAllowedEntities(@Nullable Collection<EntityType> entities);

    /**
     * Set the entity types (represented as an entity {@link Tag}) which can
     * equip this item.
     *
     * @param tag the entity tag
     * @throws IllegalArgumentException if the passed {@code tag} is not an entity
     * tag
     */
    void setAllowedEntities(@Nullable Tag<EntityType> tag);

    /**
     * Gets whether the item can be equipped by a dispenser.
     *
     * @return equippable status
     */
    boolean isDispensable();

    /**
     * Sets whether the item can be equipped by a dispenser.
     *
     * @param dispensable new equippable status
     */
    void setDispensable(boolean dispensable);

    /**
     * Gets if the item is swappable by right clicking.
     *
     * @return swappable status
     */
    boolean isSwappable();

    /**
     * Sets if the item is swappable by right clicking.
     *
     * @param swappable new status
     */
    void setSwappable(boolean swappable);

    /**
     * Gets if the item will be damaged when the wearing entity is damaged.
     *
     * @return whether the item will be damaged
     */
    boolean isDamageOnHurt();

    /**
     * Sets if the item will be damaged when the wearing entity is damaged.
     *
     * @param damage whether the item will be damaged
     */
    void setDamageOnHurt(boolean damage);

    /**
     * Gets if the item should be equipped when interacting with an entity.
     *
     * @return whether the item equips on interact
     */
    boolean isEquipOnInteract();

    /**
     * Sets if the item should be equipped when interacting with an entity.
     *
     * @param equip whether the item equips on interact
     */
    void setEquipOnInteract(boolean equip);
}
