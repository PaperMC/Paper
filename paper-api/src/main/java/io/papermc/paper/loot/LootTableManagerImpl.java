package io.papermc.paper.loot;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.loot.LootTable;

import java.util.Collection;

/**
 * Implementation of LootTableManager.
 */
class LootTableManagerImpl implements LootTableManager {
    
    private static LootTableManager instance;
    
    private LootTableManagerImpl() {
        // Private constructor for singleton
    }
    
    static LootTableManager getInstance() {
        if (instance == null) {
            // Try to use server implementation if available
            try {
                Class<?> serverImplClass = Class.forName("io.papermc.paper.loot.PaperLootTableManagerImpl");
                instance = (LootTableManager) serverImplClass.getMethod("getInstance").invoke(null);
            } catch (Exception e) {
                // Fall back to API implementation
                instance = new LootTableManagerImpl();
            }
        }
        return instance;
    }

    @Override
    public LootGenerator createGenerator(LootTable lootTable) {
        return new LootGeneratorImpl(lootTable);
    }

    @Override
    public LootGenerator createGenerator(NamespacedKey key) {
        LootTable lootTable = getLootTable(key);
        return lootTable != null ? new LootGeneratorImpl(lootTable) : null;
    }

    @Override
    public LootTable getLootTable(NamespacedKey key) {
        return Bukkit.getLootTable(key);
    }

    @Override
    public boolean hasLootTable(NamespacedKey key) {
        return getLootTable(key) != null;
    }

    @Override
    public Collection<NamespacedKey> getLootTableKeys() {
        // This would need to be implemented by accessing the server's loot table registry
        // For now, return an empty collection as a placeholder
        return java.util.Collections.emptyList();
    }

    @Override
    public LootContextBuilder createContextBuilder(org.bukkit.Location location) {
        return LootContextBuilder.create(location);
    }
}
