
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.CraftServer;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private EntityPlayer entity;
    private CraftInventoryPlayer inventory;

    public CraftHumanEntity(final CraftServer server, final EntityPlayer entity) {
        super(server, entity);
        this.entity = entity;
        this.inventory = new CraftInventoryPlayer( entity.an );
    }

    public String getName() {
        return entity.aw;
    }

    @Override
    public EntityPlayer getHandle() {
        return entity;
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle((EntityPlayer)entity);
        this.entity = entity;
        this.inventory = new CraftInventoryPlayer( entity.an );
    }

    public CraftInventoryPlayer getInventory() {
        return inventory;
    }

    public CraftItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }
}
