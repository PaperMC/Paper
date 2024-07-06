package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.SaddledHorseInventory;

public class CraftSaddledInventory extends CraftInventoryAbstractHorse implements SaddledHorseInventory {

    // Paper start - combine both inventories
    public CraftSaddledInventory(Container inventory, final Container bodyArmor) {
        super(inventory, bodyArmor);
        // Paper end - combine both inventories
    }

}
