
package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityHuman;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.CraftServer;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;

    public CraftHumanEntity(final CraftServer server, final EntityHuman entity) {
        super(server, entity);
        this.inventory = new CraftInventoryPlayer( entity.inventory );
    }

    public String getName() {
        return getHandle().name;
    }

    @Override
    public EntityHuman getHandle() {
        return (EntityHuman) entity;
    }

    public void setHandle(final EntityHuman entity) {
        super.setHandle((EntityHuman)entity);
        this.entity = entity;
        this.inventory = new CraftInventoryPlayer( entity.inventory );
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

    @Override
    public boolean isSleeping() {
        return getHandle().sleeping;
    }

    @Override
    public int getSleepTicks() {
        return getHandle().sleepTicks;
    }
}
