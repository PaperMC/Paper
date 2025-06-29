package io.papermc.paper.loot;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.loot.LootTable;

/**
 * Factory for creating loot generators and accessing loot tables safely.
 * This provides the main entry point for the new loot table API.
 * 
 * @since 1.21.4
 */
public interface LootTableManager {

    /**
     * Gets the loot table manager instance.
     * 
     * @return the loot table manager
     */
    static LootTableManager getInstance() {
        return LootTableManagerImpl.getInstance();
    }

    /**
     * Creates a loot generator for the specified loot table.
     * 
     * @param lootTable the loot table
     * @return a new loot generator
     */
    LootGenerator createGenerator(LootTable lootTable);

    /**
     * Creates a loot generator for the loot table with the specified key.
     * 
     * @param key the loot table key
     * @return a new loot generator, or null if the loot table doesn't exist
     */
    LootGenerator createGenerator(NamespacedKey key);

    /**
     * Gets a loot table by its key.
     * 
     * @param key the loot table key
     * @return the loot table, or null if it doesn't exist
     */
    LootTable getLootTable(NamespacedKey key);

    /**
     * Checks if a loot table exists with the specified key.
     * 
     * @param key the loot table key
     * @return true if the loot table exists
     */
    boolean hasLootTable(NamespacedKey key);

    /**
     * Gets all available loot table keys.
     * 
     * @return a collection of all loot table keys
     */
    java.util.Collection<NamespacedKey> getLootTableKeys();

    /**
     * Creates a loot context builder.
     * This is a convenience method equivalent to {@link LootContextBuilder#create(org.bukkit.Location)}.
     * 
     * @param location the location for the loot context
     * @return a new loot context builder
     */
    LootContextBuilder createContextBuilder(org.bukkit.Location location);
}
