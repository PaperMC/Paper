package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the equippable properties of an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#EQUIPPABLE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Equippable extends BuildableDataComponent<Equippable, Equippable.Builder> {

    /**
     * Creates a new {@link Equippable.Builder} instance.
     *
     * @param slot The slot for the new equippable to be equippable in.
     * @return a new builder
     */
    @Contract(value = "_ -> new", pure = true)
    static Equippable.Builder equippable(final EquipmentSlot slot) {
        return ItemComponentTypesBridge.bridge().equippable(slot);
    }

    /**
     * Gets the equipment slot this item can be equipped in.
     *
     * @return the equipment slot
     */
    @Contract(pure = true)
    EquipmentSlot slot();

    /**
     * Gets the equip sound key.
     *
     * @return the equip sound key
     */
    @Contract(pure = true)
    Key equipSound();

    /**
     * Gets the asset id if present.
     *
     * @return the asset id or null
     */
    @Contract(pure = true)
    @Nullable Key assetId();

    /**
     * Gets the camera overlay key if present.
     *
     * @return the camera overlay key or null
     */
    @Contract(pure = true)
    @Nullable Key cameraOverlay();

    /**
     * Gets the set of allowed entities that can equip this item.
     * May be null if all entities are allowed.
     *
     * @return the set of allowed entities
     */
    @Contract(pure = true)
    @Nullable RegistryKeySet<EntityType> allowedEntities();

    /**
     * Checks if the item is dispensable.
     *
     * @return true if dispensable, false otherwise
     */
    @Contract(pure = true)
    boolean dispensable();

    /**
     * Checks if the item is swappable.
     *
     * @return true if swappable, false otherwise
     */
    @Contract(pure = true)
    boolean swappable();

    /**
     * Checks if the item takes damage when the wearer is hurt.
     *
     * @return true if it damages on hurt, false otherwise
     */
    @Contract(pure = true)
    boolean damageOnHurt();

    /**
     * Checks if the item should be equipped when interacting with an entity.
     *
     * @return true if it equips on interact, false otherwise
     */
    @Contract(pure = true)
    boolean equipOnInteract();

    /**
     * Checks if the item can be sheared off an entity.
     *
     * @return true if can be sheared off an entity, false otherwise
     */
    @Contract(pure = true)
    boolean canBeSheared();

    /**
     * Returns the sound that is played when shearing this equipment off an entity.
     *
     * @return shear sound
     */
    @Contract(pure = true)
    Key shearSound();

    /**
     * Builder for {@link Equippable}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<Equippable> {

        /**
         * Sets the equip sound key for this item.
         *
         * @param sound the equip sound key
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder equipSound(Key sound);

        /**
         * Sets the asset id for this item.
         *
         * @param assetId the asset id, nullable
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(@Nullable Key assetId);

        /**
         * Sets the camera overlay key for this item.
         *
         * @param cameraOverlay the camera overlay key, nullable
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder cameraOverlay(@Nullable Key cameraOverlay);

        /**
         * Sets the allowed entities that can equip this item.
         *
         * @param allowedEntities the set of allowed entity types, or null if any
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder allowedEntities(@Nullable RegistryKeySet<EntityType> allowedEntities);

        /**
         * Sets whether the item is dispensable.
         *
         * @param dispensable true if dispensable
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder dispensable(boolean dispensable);

        /**
         * Sets whether the item is swappable.
         *
         * @param swappable true if swappable
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder swappable(boolean swappable);

        /**
         * Sets whether the item takes damage when the wearer is hurt.
         *
         * @param damageOnHurt true if it damages on hurt
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder damageOnHurt(boolean damageOnHurt);

        /**
         * Sets whether the item should be equipped when interacting with an entity.
         *
         * @param equipOnInteract true if it equips on interact
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder equipOnInteract(boolean equipOnInteract);

        /**
         * Sets whether the item can be sheared off an entity.
         *
         * @param canBeSheared true if can be sheared off an entity
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder canBeSheared(boolean canBeSheared);

        /**
         * Sets the sound that is played when shearing this equipment off an entity.
         *
         * @param shearSound the shear sound key
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder shearSound(Key shearSound);
    }
}
