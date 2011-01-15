
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.ItemStack;
import org.bukkit.PlayerInventory;
import org.bukkit.craftbukkit.CraftInventoryPlayer;
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
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }
}
