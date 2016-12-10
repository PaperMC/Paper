package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IInventory;

import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SaddledHorseInventory;

public class CraftSaddledInventory extends CraftInventoryAbstractHorse implements SaddledHorseInventory {

    public CraftSaddledInventory(IInventory inventory) {
        super(inventory);
    }

}
