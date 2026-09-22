package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/**
 * Represents the visibility provided by an item with the appropriate {@link DataComponentTypes#EQUIPPABLE} component,
 * modifying the range at which mobs are able to detect an entity.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#MOB_VISIBILITY
 */
@ApiStatus.NonExtendable
public interface MobVisibility {

    @Contract(value = "_, _ -> new", pure = true)
    static MobVisibility mobVisibility(final RegistryKeySet<EntityType> targetingEntityTypes, final @Range(from = 0, to = 10) float visibility) {
        return ItemComponentTypesBridge.bridge().mobVisibility(targetingEntityTypes, visibility);
    }

    /**
     * Gets the set of entity types that this item modifies the visibility for.
     *
     * @return the set of entity types
     */
    @Contract(pure = true)
    RegistryKeySet<EntityType> targetingEntityTypes();

    /**
     * Gets the visibility provided by this item, modifying the range at which mobs are able to detect an entity.
     *
     * @return the visibility
     */
    @Contract(pure = true)
    @Range(from = 0, to = 10) float visibility();
}
