package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.SaddledHorseInventory;

public class CraftSaddledInventory extends CraftInventoryAbstractHorse implements SaddledHorseInventory {

    public CraftSaddledInventory(Container inventory, final Container bodyArmorInventory, final Container saddleInventory) {
        super(inventory, bodyArmorInventory, saddleInventory);
    }

}
