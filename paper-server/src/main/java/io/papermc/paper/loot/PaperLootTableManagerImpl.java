package io.papermc.paper.loot;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.loot.LootTable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Server-side implementation of LootTableManager.
 */
public class PaperLootTableManagerImpl implements LootTableManager {
    
    private static final LootTableManager INSTANCE = new PaperLootTableManagerImpl();
    
    private PaperLootTableManagerImpl() {
        // Private constructor for singleton
    }
    
    public static LootTableManager getInstance() {
        return INSTANCE;
    }

    @Override
    public LootGenerator createGenerator(LootTable lootTable) {
        Preconditions.checkNotNull(lootTable, "LootTable cannot be null");
        return new PaperLootGeneratorImpl(lootTable);
    }

    @Override
    public LootGenerator createGenerator(NamespacedKey key) {
        LootTable lootTable = getLootTable(key);
        return lootTable != null ? new PaperLootGeneratorImpl(lootTable) : null;
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
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return java.util.Collections.emptyList();
        }
        
        return server.reloadableRegistries()
            .get()
            .registryOrThrow(Registries.LOOT_TABLE)
            .keySet()
            .stream()
            .map(CraftNamespacedKey::fromMinecraft)
            .collect(Collectors.toList());
    }

    @Override
    public LootContextBuilder createContextBuilder(org.bukkit.Location location) {
        return new PaperLootContextBuilderImpl(location);
    }
}
