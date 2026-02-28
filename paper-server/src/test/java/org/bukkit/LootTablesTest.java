package org.bukkit;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllFeatures
@Deprecated
public class LootTablesTest {

    @Test
    public void testLootTablesEnumExists() {
        Registry<LootTable> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.LOOT_TABLE);
        LootTables[] tables = LootTables.values();

        for (LootTables table : tables) {
            LootTable lootTable = registry.get(table.getKey());

            assertNotNull(lootTable, "Unknown LootTable " + table.getKey());
            assertEquals(lootTable.getKey(), table.getKey());
        }
    }

    @Test
    public void testNMS() {
        for (ResourceKey<net.minecraft.world.level.storage.loot.LootTable> key : net.minecraft.world.level.storage.loot.BuiltInLootTables.all()) {
            NamespacedKey bukkitKey = CraftNamespacedKey.fromMinecraft(key.identifier());
            LootTables lootTable = Registry.LOOT_TABLES.get(bukkitKey);

            assertNotNull(lootTable, "Unknown LootTable " + key);
            assertEquals(lootTable.getKey(), bukkitKey);
        }
    }
}
