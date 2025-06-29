package io.papermc.paper.loot;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

/**
 * Represents a comprehensive loot context that provides all necessary
 * information for loot table generation.
 * <p>
 * This replaces the limited {@link org.bukkit.loot.LootContext} and provides
 * access to all Minecraft loot context parameters safely.
 * 
 * @since 1.21.4
 */
public interface LootContext {

    /**
     * Gets the location where loot generation occurs.
     * 
     * @return the location
     */
    Location getLocation();

    /**
     * Gets the luck modifier for loot generation.
     * 
     * @return the luck value
     */
    float getLuck();

    /**
     * Gets the entity that was killed or interacted with.
     * 
     * @return the looted entity, or null if not applicable
     */
    Entity getLootedEntity();

    /**
     * Gets the entity that killed or interacted with the target.
     * 
     * @return the killer entity, or null if not applicable
     */
    HumanEntity getKiller();
}
