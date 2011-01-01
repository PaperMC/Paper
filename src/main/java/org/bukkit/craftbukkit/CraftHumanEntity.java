
package org.bukkit.craftbukkit;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryPlayer;
import org.bukkit.HumanEntity;
import org.bukkit.ItemStack;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private EntityPlayer entity;

    public CraftHumanEntity(final CraftServer server, final EntityPlayer entity) {
        super(server, entity);
        this.entity = entity;
    }

    public ItemStack getSelectedItem() {
        // TODO: Implement inventories
        final InventoryPlayer inventory = entity.an;
        return new ItemStack(inventory.e().c, inventory.e().a);
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

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityID() + "name=" + getName() + '}';
    }
}
