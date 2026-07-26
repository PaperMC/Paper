package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.SaddledHorseInventory;

public class CraftInventorySaddledHorse extends CraftInventorySaddledMount implements SaddledHorseInventory {

    public CraftInventorySaddledHorse(Container inventory, final Container bodyArmorInventory, final Container saddleInventory) {
        super(inventory, bodyArmorInventory, saddleInventory);
    }

}
