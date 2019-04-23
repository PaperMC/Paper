package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PlayerInventory;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class PlayerInventoryTest extends AbstractTestingBase {

    @Test
    public void testCanHold() throws Exception {
        ItemStack itemStackApple = new ItemStack(Items.APPLE);
        ItemStack itemStack1Coal = new ItemStack(Items.COAL);
        ItemStack itemStack32Coal = new ItemStack(Items.COAL, 32);
        ItemStack itemStack63Coal = new ItemStack(Items.COAL, 63);
        ItemStack itemStack64Coal = new ItemStack(Items.COAL, 64);

        // keep one slot empty
        PlayerInventory inventory = new PlayerInventory(null);
        for (int i = 0; i < inventory.items.size() - 1; i++) {
            inventory.setItem(i, itemStackApple);
        }

        // one slot empty
        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(32, inventory.canHold(itemStack32Coal));
        assertEquals(64, inventory.canHold(itemStack64Coal));

        // no free space with a stack of the item to check in the inventory
        inventory.setItem(inventory.items.size() - 1, itemStack64Coal);

        assertEquals(0, inventory.canHold(itemStack1Coal));
        assertEquals(0, inventory.canHold(itemStack32Coal));
        assertEquals(0, inventory.canHold(itemStack64Coal));

        // no free space without a stack of the item to check in the inventory
        inventory.setItem(inventory.items.size() - 1, itemStackApple);

        assertEquals(0, inventory.canHold(itemStack1Coal));
        assertEquals(0, inventory.canHold(itemStack32Coal));
        assertEquals(0, inventory.canHold(itemStack64Coal));

        // free space for 32 items in one slot
        inventory.setItem(inventory.items.size() - 1, itemStack32Coal);

        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(32, inventory.canHold(itemStack32Coal));
        assertEquals(32, inventory.canHold(itemStack64Coal));

        // free space for 1 item in two slots
        inventory.setItem(inventory.items.size() - 1, itemStack63Coal);
        inventory.setItem(inventory.items.size() - 2, itemStack63Coal);

        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(2, inventory.canHold(itemStack32Coal));
        assertEquals(2, inventory.canHold(itemStack64Coal));
    }
}
