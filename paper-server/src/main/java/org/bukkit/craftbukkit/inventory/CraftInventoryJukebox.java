package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.bukkit.block.Jukebox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.JukeboxInventory;

public class CraftInventoryJukebox extends CraftInventory implements JukeboxInventory {

    public CraftInventoryJukebox(JukeboxBlockEntity inventory) {
        super(inventory);
    }

    @Override
    public void setRecord(ItemStack item) {
        this.setItem(0, item);
    }

    @Override
    public ItemStack getRecord() {
        return this.getItem(0);
    }

    @Override
    public Jukebox getHolder() {
        return (Jukebox) this.inventory.getOwner();
    }
}
