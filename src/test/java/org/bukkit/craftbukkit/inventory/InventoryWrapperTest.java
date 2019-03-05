package org.bukkit.craftbukkit.inventory;

import junit.framework.Assert;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

public class InventoryWrapperTest {

    @Test
    public void test() {
        Inventory raw = new CraftInventoryCustom(null, 9);
        raw.addItem(new ItemStack(Material.STONE));

        Assert.assertTrue(raw.contains(Material.STONE));

        InventoryWrapper wrapper = new InventoryWrapper(raw);
        CraftInventory proxy = new CraftInventory(wrapper);

        Assert.assertTrue(proxy.contains(Material.STONE));
    }
}
