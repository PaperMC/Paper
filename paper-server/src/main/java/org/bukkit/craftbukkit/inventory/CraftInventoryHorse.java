package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftSaddledInventory implements HorseInventory {

    public CraftInventoryHorse(Container inventory, Container bodyArmorInventory, Container saddleInventory) {
        super(inventory, bodyArmorInventory, saddleInventory);
    }
}
