package org.bukkit.craftbukkit.inventory;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.PlayerEquipment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class PlayerInventoryTest {

    @Test
    public void testCanHold() throws Exception {
        ItemStack itemStackApple = new ItemStack(Items.APPLE);
        ItemStack itemStack1Coal = new ItemStack(Items.COAL);
        ItemStack itemStack32Coal = new ItemStack(Items.COAL, 32);
        ItemStack itemStack63Coal = new ItemStack(Items.COAL, 63);
        ItemStack itemStack64Coal = new ItemStack(Items.COAL, 64);

        // keep one slot empty
        Inventory inventory = new Inventory(null, new PlayerEquipment(null));
        for (int i = 0; i < inventory.getNonEquipmentItems().size() - 1; i++) {
            inventory.setItem(i, itemStackApple);
        }

        // one slot empty
        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(32, inventory.canHold(itemStack32Coal));
        assertEquals(64, inventory.canHold(itemStack64Coal));

        // no free space with a stack of the item to check in the inventory
        inventory.setItem(inventory.getNonEquipmentItems().size() - 1, itemStack64Coal);

        assertEquals(0, inventory.canHold(itemStack1Coal));
        assertEquals(0, inventory.canHold(itemStack32Coal));
        assertEquals(0, inventory.canHold(itemStack64Coal));

        // no free space without a stack of the item to check in the inventory
        inventory.setItem(inventory.getNonEquipmentItems().size() - 1, itemStackApple);

        assertEquals(0, inventory.canHold(itemStack1Coal));
        assertEquals(0, inventory.canHold(itemStack32Coal));
        assertEquals(0, inventory.canHold(itemStack64Coal));

        // free space for 32 items in one slot
        inventory.setItem(inventory.getNonEquipmentItems().size() - 1, itemStack32Coal);

        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(32, inventory.canHold(itemStack32Coal));
        assertEquals(32, inventory.canHold(itemStack64Coal));

        // free space for 1 item in two slots
        inventory.setItem(inventory.getNonEquipmentItems().size() - 1, itemStack63Coal);
        inventory.setItem(inventory.getNonEquipmentItems().size() - 2, itemStack63Coal);

        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(2, inventory.canHold(itemStack32Coal));
        assertEquals(2, inventory.canHold(itemStack64Coal));

        // free space for 32 items in non-empty off-hand slot
        inventory.setItem(inventory.getNonEquipmentItems().size() - 1, itemStackApple);
        inventory.setItem(inventory.getNonEquipmentItems().size() - 2, itemStackApple);
        inventory.setItem(inventory.getNonEquipmentItems().size() + inventory.getArmorContents().size(), itemStack32Coal);

        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(32, inventory.canHold(itemStack32Coal));
        assertEquals(32, inventory.canHold(itemStack64Coal));

        // free space for 1 item in non-empty off-hand slot and another slot
        inventory.setItem(inventory.getNonEquipmentItems().size() - 1, itemStack63Coal);
        inventory.setItem(inventory.getNonEquipmentItems().size() + inventory.getArmorContents().size(), itemStack63Coal);

        assertEquals(1, inventory.canHold(itemStack1Coal));
        assertEquals(2, inventory.canHold(itemStack32Coal));
        assertEquals(2, inventory.canHold(itemStack64Coal));
    }
}
