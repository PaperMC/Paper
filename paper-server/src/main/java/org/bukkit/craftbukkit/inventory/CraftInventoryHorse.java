package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftSaddledInventory implements HorseInventory {

    // Paper start - properly combine both inventories
    public CraftInventoryHorse(Container inventory, Container bodyArmorInventory) {
        super(inventory, bodyArmorInventory);
    }
    // Paper end - properly combine both inventories
}
