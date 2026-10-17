package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.HorseInventory;

public class CraftInventoryHorse extends CraftInventorySaddledHorse implements HorseInventory {

    public CraftInventoryHorse(Container inventory, Container bodyArmorInventory, Container saddleInventory) {
        super(inventory, bodyArmorInventory, saddleInventory);
    }
}
