package org.bukkit.craftbukkit;

import net.minecraft.world.Container;
import org.bukkit.craftbukkit.inventory.CraftInventorySaddledMount;
import org.bukkit.inventory.ArmoredSaddledMountInventory;

public class CraftInventoryArmoredSaddledMount extends CraftInventorySaddledMount implements ArmoredSaddledMountInventory {

    public CraftInventoryArmoredSaddledMount(final Container inventory, final Container bodyArmorInventory, final Container saddleInventory) {
        super(inventory, bodyArmorInventory, saddleInventory);
    }
}
