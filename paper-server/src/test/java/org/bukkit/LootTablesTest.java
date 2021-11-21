package org.bukkit;

import net.minecraft.resources.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Assert;
import org.junit.Test;

public class LootTablesTest extends AbstractTestingBase {

    @Test
    public void testLootTablesEnumExists() {
        LootTables[] tables = LootTables.values();

        for (LootTables table : tables) {
            LootTable lootTable = Bukkit.getLootTable(table.getKey());

            Assert.assertNotNull("Unknown LootTable " + table.getKey(), lootTable);
            Assert.assertEquals(lootTable.getKey(), table.getKey());
        }
    }

    @Test
    public void testNMS() {
        for (MinecraftKey key : net.minecraft.world.level.storage.loot.LootTables.all()) {
            NamespacedKey bukkitKey = CraftNamespacedKey.fromMinecraft(key);
            LootTables lootTable = Registry.LOOT_TABLES.get(bukkitKey);

            Assert.assertNotNull("Unknown LootTable " + key, lootTable);
            Assert.assertEquals(lootTable.getKey(), bukkitKey);
        }
    }
}
