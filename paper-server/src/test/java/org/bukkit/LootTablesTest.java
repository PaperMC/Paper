package org.bukkit;

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
}
